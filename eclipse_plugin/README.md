# Woowacourse Java Format Eclipse Plugin

## Enabling

See https://github.com/yhkee0404/woowacourse-java-format#eclipse

## Development

### Prerequisites

[META-INF/MANIFEST.MF][]를 직접 수정할 필요가 없습니다. [eclipse_plugin/pom.xml][]을 수정하고 다음을 실행하세요:

```zsh
mvn tycho-versions:update-eclipse-metadata
```

[META-INF/MANIFEST.MF]: https://github.com/yhkee0404/woowacourse-java-format/blob/main/eclipse_plugin/META-INF/MANIFEST.MF
[eclipse_plugin/pom.xml]: https://github.com/yhkee0404/woowacourse-java-format/blob/main/eclipse_plugin/pom.xml

### Building the Plugin

[빌드해 보기][]를 따릅니다. 더 자세한 내용은 [원문 안내][]를 참고하세요.

[빌드해 보기]: https://github.com/yhkee0404/woowacourse-java-format/blob/main/README.md#빌드해-보기
[원문 안내]: https://github.com/google/google-java-format/blob/master/eclipse_plugin/README.md
