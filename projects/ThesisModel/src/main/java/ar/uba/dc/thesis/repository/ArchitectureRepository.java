package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.acme.Architecture;

public class ArchitectureRepository {

	public static Architecture getDummyArchitecture() {
		return new Architecture(ComponentRepository.getPageRenderer());
	}

	public static Architecture getZnnArchitecture() {
		return new Architecture(ComponentRepository.getPageRenderer());
	}
}
