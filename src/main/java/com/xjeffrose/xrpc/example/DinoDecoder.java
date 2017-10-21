package com.xjeffrose.xrpc.example;

import java.io.IOException;

public class DinoDecoder {

  public static void main(String[] args) throws IOException {

    byte[] bytes = new byte[System.in.available()];
    System.in.read(bytes, 0, bytes.length);
    DinoResponse dino = DinoResponse.ADAPTER.decode(bytes);

    System.out.println(dino);
  }

}
