package com.cas.sim.tis.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.net.HostAndPort;

import io.airlift.drift.client.DriftClientFactory;
import io.airlift.drift.client.address.AddressSelector;
import io.airlift.drift.client.address.SimpleAddressSelector;
import io.airlift.drift.codec.ThriftCodecManager;
import io.airlift.drift.transport.client.Address;
import io.airlift.drift.transport.netty.client.DriftNettyClientConfig;
import io.airlift.drift.transport.netty.client.DriftNettyMethodInvokerFactory;

@Configuration
public class SwiftConfig {

	@Value(value = "${server.swift.port}")
	private Integer port;

	@Value(value = "${server.base.address}")
	private String host;

	@Bean
	public DriftClientFactory thriftClientManager() {
		// server address
		List<HostAndPort> addresses = ImmutableList.of(HostAndPort.fromParts(host, port));

		// expensive services that should only be created once
		ThriftCodecManager codecManager = new ThriftCodecManager();
		AddressSelector<Address> addressSelector = new SimpleAddressSelector(addresses);
		DriftNettyClientConfig config = new DriftNettyClientConfig();
		// methodInvokerFactory must be closed
		DriftNettyMethodInvokerFactory<?> methodInvokerFactory = DriftNettyMethodInvokerFactory.createStaticDriftNettyMethodInvokerFactory(config);

		// client factory
		DriftClientFactory clientFactory = new DriftClientFactory(codecManager, methodInvokerFactory, addressSelector);
		return clientFactory;
	}

}
