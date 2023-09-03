package com.example.saastemplate;

import com.example.saastemplate.authzd.annotation.Authz;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThingController {

    @Authz(objectId = "#thingId", objectType = "THING", action = "READ")
    @PostMapping("/thing/{thingId}")
    public ResponseEntity<Void> getThings(@PathVariable String thingId){

    }
}
