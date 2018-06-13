package org.springframework.boot.autoconfigure.elf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "elf"
)
public class ElfProperties {

    String name="y";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
