package com.digregorio.gw2tinkering.domain;

public class GWTinkerApi extends BaseService<GWTinkerService> {

    public static final String BASE_URL = "https://api.guildwars2.com/";

    public GWTinkerApi() {
        super(GWTinkerService.class, BASE_URL);
    }

}
