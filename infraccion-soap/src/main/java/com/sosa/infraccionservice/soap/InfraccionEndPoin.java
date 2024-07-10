package com.sosa.infraccionservice.soap;

import com.sosa.infraccionservice.AddInfraccionRequest;
import com.sosa.infraccionservice.AddInfraccionResponse;
import com.sosa.infraccionservice.DeleteInfraccionRequest;
import com.sosa.infraccionservice.DeleteInfraccionResponse;
import com.sosa.infraccionservice.GetAllInfraccionesRequest;
import com.sosa.infraccionservice.GetAllInfraccionesResponse;
import com.sosa.infraccionservice.GetInfraccionRequest;
import com.sosa.infraccionservice.GetInfraccionResponse;
import com.sosa.infraccionservice.ServiceStatus;
import com.sosa.infraccionservice.UpdateInfraccionRequest;
import com.sosa.infraccionservice.UpdateInfraccionResponse;
import com.sosa.infraccionservice.entity.infracciones;
import com.sosa.infraccionservice.service.InfraccionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class InfraccionEndPoint {

    @Autowired
    private InfraccionService service;

    @PayloadRoot(namespace = "http://www.sosa.com/infracciones-soap", localPart = "GetAllInfraccionesRequest")
    @ResponsePayload
    public GetAllInfraccionesResponse findAll(@RequestPayload GetAllInfraccionesRequest request) {
        GetAllInfraccionesResponse response = new GetAllInfraccionesResponse();

        Pageable page = PageRequest.of(request.getOffset(), request.getLimit());
        List<Infraccion> infracciones;
        if (request.getDni() == null) {
            infracciones = service.findAll(page);
        } else {
            infracciones = service.findByDni(request.getDni(), page);
        }

        List<InfraccionDetalle> infraccionesResponse = new ArrayList<>();
        for (Infraccion infraccion : infracciones) {
            InfraccionDetalle detalle = new InfraccionDetalle();
            BeanUtils.copyProperties(infraccion, detalle);
            infraccionesResponse.add(detalle);
        }
        response.getInfraccionDetalle().addAll(infraccionesResponse);
        return response;
    }

    @PayloadRoot(namespace = "http://www.sosa.com/infracciones-soap", localPart = "GetInfraccionRequest")
    @ResponsePayload
    public GetInfraccionResponse findById(@RequestPayload GetInfraccionRequest request) {
        GetInfraccionResponse response = new GetInfraccionResponse();
        Infraccion infraccion = service.findById(request.getId());
        if (infraccion != null) {
            InfraccionDetalle detalle = new InfraccionDetalle();
            BeanUtils.copyProperties(infraccion, detalle);
            response.setInfraccionDetalle(detalle);
        }
        return response;
    }

    @PayloadRoot(namespace = "http://www.sosa.com/infracciones-soap", localPart = "AddInfraccionRequest")
    @ResponsePayload
    public AddInfraccionResponse create(@RequestPayload AddInfraccionRequest request) {
        AddInfraccionResponse response = new AddInfraccionResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        Infraccion infraccion = new Infraccion();
        infraccion.setDni(request.getDni());
        infraccion.setFecha(request.getFecha());
        infraccion.setTipoInfraccion(request.getTipoInfraccion());
        infraccion.setUbicacion(request.getUbicacion());
        infraccion.setDescripcion(request.getDescripcion());
        infraccion.setMontoMulta(request.getMontoMulta());
        infraccion.setEstado(request.getEstado());

        Infraccion savedInfraccion = service.save(infraccion);

        if (savedInfraccion != null) {
            InfraccionDetalle detalle = new InfraccionDetalle();
            BeanUtils.copyProperties(savedInfraccion, detalle);
            response.setInfraccionDetalle(detalle);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Infraccion added successfully.");
        } else {
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Infraccion
