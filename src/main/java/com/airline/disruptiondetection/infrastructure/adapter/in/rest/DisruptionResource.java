package com.airline.disruptiondetection.infrastructure.adapter.in.rest;

import com.airline.disruptiondetection.domain.model.Disruption;
import com.airline.disruptiondetection.domain.port.out.DisruptionRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/disruptions")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisruptionResource {

    private final DisruptionRepositoryPort disruptionRepository;

    public DisruptionResource(DisruptionRepositoryPort disruptionRepository) {
        this.disruptionRepository = disruptionRepository;
    }

    @GET
    public List<Disruption> getAll() {
        return disruptionRepository.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        return disruptionRepository.findById(id)
            .map(d -> Response.ok(d).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/active")
    public List<Disruption> getActive() {
        return disruptionRepository.findActive();
    }
}