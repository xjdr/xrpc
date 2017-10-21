package com.xjeffrose.xrpc.example;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

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
import java.util.function.Function;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import okio.ByteString;

@Slf4j
public class REST {
    static final Router router = new Router();
    static final List<Person> people = new ArrayList<>();

  // See https://github.com/square/moshi for the Moshi Magic
    static final Moshi moshi = new Moshi.Builder().build();
    static final Type type = Types.newParameterizedType(List.class, Person.class);
    static final JsonAdapter<List<Person>> adapter = moshi.adapter(type);

  public static void main(String[] args) {

    // Define your REST endpoing
    Function<FullHttpRequest, HttpResponse> peopleHandler = req -> {
      ByteBuf bb = Unpooled.compositeBuffer();

      switch (req.method().name()) {
        case "GET":
          bb.writeBytes(adapter.toJson(people).getBytes(Charset.forName("UTF-8")));
          break;

        case "PUT":

          break;

        case "POST":
          Person p = new Person(req.content().toString(Charset.forName("UTF-8")));
          people.add(p);

          bb.writeBytes("ok".getBytes(Charset.forName("UTF-8")));
          break;

        case "DELETE":

          break;

        default:

          break;

       }

      return Recipes.newResponseOk(bb);
    };

    router.addRoute("/people", peopleHandler);

   try {
      // Fire away
      router.listenAndServe();
    } catch (IOException e) {
      log.error("Failed to start people server", e);
    }

  }

}
