package ar.uba.dc.thesis;

import ar.uba.dc.thesis.acme.Architecture;

public class ArchitectureRepository {

	public static Architecture getDemoArchitecture() {
		return new Architecture(ComponentRepository.getPageRenderer());
	}
}
