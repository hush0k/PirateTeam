package com.hush0k.pirateTeam.pirate.enums;

import lombok.Getter;

@Getter
public enum Country {

    //EUROPE
    ENGLAND("England", "GB"),
    SPAIN("Spain", "ES"),
    FRANCE("France", "FR"),
    NETHERLANDS("Netherlands", "NL"),
    PORTUGAL("Portugal", "PT"),

    //AMERICA
    USA("United States", "US"),
    BAHAMAS("Bahamas", "BS"),
    HAITI("Haiti", "HT"),

    //AFRICA
    MOROCCO("Morocco", "MA"),
    SOMALIA("Somalia", "SO"),

    //NEAR EAST
    TURKEY("Turkey", "TR"),
    ARABIC("Arabic", "AR");

    private final String name;
    private final String code;

    Country(String name, String code) {
        this.name = name;
        this.code = code;
    }
}