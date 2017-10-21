package com.xjeffrose.xrpc.example;

import java.io.IOException;

public class DinoEncoder {

  public static void main(String[] args) throws IOException {

    Dino dino = new Dino.Builder()
      .name(args[0])
      .build();

    byte[] bytes = Dino.ADAPTER.encode(dino);
    System.out.write(bytes, 0, bytes.length);
  }

}
