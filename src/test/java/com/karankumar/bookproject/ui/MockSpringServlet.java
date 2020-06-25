package com.karankumar.bookproject.ui;

import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.SpringServlet;
import com.vaadin.flow.spring.SpringVaadinServletService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class MockSpringServlet extends SpringServlet {
	@NotNull
	public final Routes routes;
	@NotNull
	public final ApplicationContext ctx;

	public MockSpringServlet(@NotNull Routes routes, @NotNull ApplicationContext ctx) {
		super(ctx, false);
		this.ctx = ctx;
		this.routes = routes;
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		routes.register(servletConfig.getServletContext());
		super.init(servletConfig);
	}


	@Override
	protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
		final VaadinServletService service = new SpringVaadinServletService(this, deploymentConfiguration, ctx) {
			@Override
			protected boolean isAtmosphereAvailable() {
				return false;
			}

			@Override
			public String getMainDivId(VaadinSession session, VaadinRequest request) {
				return "ROOT-1";
			}
		};
		service.init();
		return service;
	}
}