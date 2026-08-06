package com.thamindu.task_manager.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class jwtProvider {
  private final SecretKey key;
  private final long 
}
