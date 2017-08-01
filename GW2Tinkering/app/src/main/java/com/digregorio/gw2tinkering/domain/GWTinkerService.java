package com.digregorio.gw2tinkering.domain;

/**
 * Created by jesse on 7/28/2017.
 */

import com.digregorio.gw2tinkering.model.World;
import java.util.List;
import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GWTinkerService {

    @GET("/v2/worlds")
    Observable<List<World>> getAllWorldIds();

    @GET("/v2/worlds?ids=all")
    Observable<List<World>> getWorlds();

}