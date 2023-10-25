package com.nttdata.knot.almapi.Interfaces;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.nttdata.knot.almapi.Models.PipelineStatusPackage.PipelineResponseStatus;

@Repository
public interface ITektonPipelinesRepository extends CosmosRepository<PipelineResponseStatus, String> {
    // List<PipelineResponseStatus> findByKeyContains(String searchKey);
    // @Query("SELECT key FROM tektonpipelines c WHERE CONTAINS(c._partitionKey,
    // @searchKey)")
    List<PipelineResponseStatus> findKeysByKeyContains(@Param("searchKey") String searchKey);

    PipelineResponseStatus findKeyByKeyContains(@Param("searchKey") String searchKey);

    List<PipelineResponseStatus> findByKeyStartingWith(@Param("prefix") String prefix);

    // List<PipelineResponseStatus> findFiveKeysByKeyStartingWithAndCountEqualsFive(@Param("prefix") String componentName);

    @Query(value = "SELECT * FROM tektonpipelines c WHERE c.key.value.start_time >= @startDate")
    List<PipelineResponseStatus> filterbyDate(@Param("startDate") String startDate);
}
