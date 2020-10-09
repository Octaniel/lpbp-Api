package com.bsoftware.lpbp.event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

public class RecursoCriadoEvent extends ApplicationEvent {
    private HttpServletResponse httpServletResponse;
    private Long id;

    public RecursoCriadoEvent(Object source, HttpServletResponse httpServletResponse, Long id) {
        super(source);
        this.httpServletResponse = httpServletResponse;
        this.id = id;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public Long getId() {
        return id;
    }
}
