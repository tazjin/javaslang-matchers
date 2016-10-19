Javaslang Hamcrest matchers
===========================

[![Build Status](https://travis-ci.org/tazjin/javaslang-matchers.svg?branch=master)](https://travis-ci.org/tazjin/javaslang-matchers)

Provides a set of useful Hamcrest matchers for operating on Javaslang values.

This makes it possible to easily define Hamcrest-style assertions on types such as
`Option<T>` or Try<T>`.

## Installation

This project is published on [Jitpack][]).

Add this to your `pom.xml`:

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>in.tazj</groupId>
    <artifactId>javaslang-matchers</artifactId>
    <version>1.0</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

## Usage

Consult the [documentation][] for usage instructions. Most of it should be pretty self-explanatory.

File issues and feature requests if you have any!

[documentation]: https://tazjin.github.io/javaslang-matchers/
[Jitpack]: https://jitpack.io/
