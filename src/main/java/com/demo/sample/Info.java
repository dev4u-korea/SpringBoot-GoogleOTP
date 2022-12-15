package com.demo.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Info {

  @GetMapping("/version")
  public String version() {
    return "ApiServer v0.0.1";
  }

  @GetMapping("/echo")
  public String echo(@RequestParam String msg) {
    return "Response = " + msg;
  }

  @GetMapping("/version/{idx}")
  public String version(@PathVariable Long idx) {
    return "version = " + idx;
  }
  
}
