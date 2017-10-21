package com.xjeffrose.xrpc.example;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

import static com.xjeffrose.xrpc.http.Recipes.*;

import com.xjeffrose.xrpc.http.Recipes;
import com.xjeffrose.xrpc.http.Router;
import com.xjeffrose.xrpc.http.Route;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.io.IOException;

import okio.ByteString;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RPC {
  static final Router router = new Router();
  static final List<Dino> dinos = new ArrayList<>();

  static HttpResponse addDino(FullHttpRequest req) {

    Optional<Dino> d;
    try {
      d =  Optional.of(Dino.ADAPTER.decode(ByteString.of(req.content().nioBuffer())));
      d.ifPresent(dinos::add);
    } catch (IOException e) {
      return  Recipes.newResponseBadRequest(e.getMessage());
    }

    return Recipes.newResponseOk("ok");
  }

  static HttpResponse listDinos(FullHttpRequest req) {
    ByteBuf bb = Unpooled.compositeBuffer();

    DinoResponse dr = new DinoResponse.Builder().dinos(dinos).build();

    for (Dino d : dinos) {
      log.info(d.toString());
    }

    bb.writeBytes(DinoResponse.ADAPTER.encode(dr));

    return Recipes.newResponseOk(bb, ContentType.Application_Octet_Stream);
  }

  public static void main(String[] args) {

    // Define your REST endpoing
    BiFunction<FullHttpRequest, Route, HttpResponse> dinoHandler = (req, route) -> {
      HttpResponse resp;
      String path = route.groups(req.uri()).get("method");
      switch (path) {
        case "addDino":
          return addDino(req);

        case "listDinos":
          resp = listDinos(req);
          break;

        default:
          resp = Recipes.newResponseBadRequest("Invalid Method");
          break;
      }

      return resp;
    };

    router.addRoute("/dinos/:method", dinoHandler);

   try {
      // Fire away
      router.listenAndServe();
    } catch (IOException e) {
      log.error("Failed to start people server", e);
    }

  }

}
