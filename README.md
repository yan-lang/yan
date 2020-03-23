# Yan

[![License](https://img.shields.io/github/license/yan-lang/yan)](https://opensource.org/licenses/MIT) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.yan-lang/yan-foundation/badge.svg)](https://search.maven.org/artifact/com.github.yan-lang/yan-foundation)  [![Build Status](https://travis-ci.com/yan-lang/yan.svg?branch=master)](https://travis-ci.com/yan-lang/yan) ![Java CI with Maven](https://github.com/yan-lang/yan/workflows/Java%20CI%20with%20Maven/badge.svg) 

Yan is composed of several different modules from three different sub-projects.

> Yan = Yan Compiler + Yan foundation + Yan Common

**Yan Compiler** is the compiler for [Yan](), which is based on *Yan foundation*.

**Yan foundation** is the backend framework of Yan compiler's implementation. It can also be used as a general compiler framework for implementations of other languages. 

**Yan Common** is a collection of common data structures and utils that might be useful in all language implementations. 

## Yan Compiler

**Yan** is a tiny educational language, which is designed as a demo for the educational compiler framwork *Yan Foundation.* The basic syntax of Yan is similar to Swift, while it behaves like a procedure-oriented language. 

### Getting Started

You can download the interpreter for Yan from the release section above, or click [download]() here. After downloaded, type in the command below to lanuch it.

```shell
java -jar --enable-preview yan.jar   # You need at least Java 13
```

We use switch-expression, which is a preview feature of Java 1, during the development, so `--enable-preview` is mandatory.

## Yan Foundation

**Yan Foundation** is the backend framework of Yan compiler's implementation. It was designed as an educational compiler framework that attempts to help students to write compilers for their own language. This framework encaupsuate most of functionities that are not directly related with compiling. In this way, students can focus on the key phases of compiler, such tokenzing, parsing, optimizing, etc. 

### Documentation

- [Yan](https://yan-lang.github.io/docs/) 

### Usage

Maven 

```xml
<dependency>
  <groupId>com.github.yan-lang</groupId>
  <artifactId>yan-foundation</artifactId>
  <version>0.1.2</version>
</dependency>
```

## Related Projects

- [Decaf]() : A similar project in Tsinghua University.

## License

Copyright (c) Yan. All rights reserved.

Licensed under the [MIT](LICENSE) license.
