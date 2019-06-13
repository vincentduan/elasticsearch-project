package cn.ac.iie.service;

public interface TokenGenerator {

    String generateWithTimeMills(String... strings);

    String generate(String... strings);

}
