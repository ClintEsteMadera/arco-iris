package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.acme.Architecture;

public class ArchitectureRepository {

	private static final Architecture ZNN_ARCHITECTURE = new Architecture(ComponentRepository.getProxy());

	public static Architecture getDummyArchitecture() {
		return ZNN_ARCHITECTURE;
	}

	public static Architecture getZnnArchitecture() {
		return ZNN_ARCHITECTURE;
	}
}
