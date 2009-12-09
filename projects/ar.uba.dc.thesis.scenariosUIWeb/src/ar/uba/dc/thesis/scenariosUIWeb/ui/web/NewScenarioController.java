package ar.uba.dc.thesis.scenariosUIWeb.ui.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.bean.BeanValidator;

import ar.uba.dc.thesis.scenariosUIWeb.common.util.EnumUtils;
import ar.uba.dc.thesis.scenariosUIWeb.domain.Concern;
import ar.uba.dc.thesis.scenariosUIWeb.domain.SelfHealingScenario;

@Controller
@SessionAttributes("scenario")
public class NewScenarioController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView setupForm() {
		return setupForm(new SelfHealingScenario());
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute
	SelfHealingScenario scenario, BindingResult bindingResult, SessionStatus status) {
		// TODO: valida pero no muestra los mensajes de error, corregir
		// beanValidator.validate(scenario, bindingResult);
		// if (bindingResult.hasErrors()) {
		// return setupForm(scenario);
		// }
		ModelAndView mav = new ModelAndView("newScenarioCreated");
		mav.addObject("concerns", getConcerns());
		mav.addObject("scenario", scenario);
		status.setComplete();
		return mav;
	}

	public ModelAndView setupForm(SelfHealingScenario scenario) {
		ModelAndView mav = new ModelAndView("newScenario");
		mav.addObject("concerns", getConcerns());
		mav.addObject("scenario", scenario);
		return mav;
	}

	private Concern[] getConcerns() {
		return EnumUtils.getEnumsOrderedByToString(Concern.class);
	}

	@Autowired
	private BeanValidator beanValidator;

}
