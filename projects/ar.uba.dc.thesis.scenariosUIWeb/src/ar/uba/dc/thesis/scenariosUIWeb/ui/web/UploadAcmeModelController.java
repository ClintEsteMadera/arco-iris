package ar.uba.dc.thesis.scenariosUIWeb.ui.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UploadAcmeModelController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView setupForm() {
		ModelAndView mav = new ModelAndView("uploadAcmeModel");
		return mav;
	}

}
