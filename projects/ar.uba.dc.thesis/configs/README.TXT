znewsWithScenarios1:
	Un escenario con una única estrategia, al comienzo el escenario se satisface, luego de unos segundos el escenario se rompe y
	se lanza la adaptacion, escogiendo la estrategia, ejecutándola y reparando el sistema. A partir de allí el escenario
	no se rompe mas.

znewsWithScenarios2:
	Un escenario con 2 estrategias, al comienzo el escenario se satisface, luego de unos segundos el escenario se rompe y
	se lanza la adaptacion, escogiendo la estrategia BruteReduceResponseTime vs VariedReduceResponseTime (ambas tienen el
	mismo score ya que no existe ningún escenario relacionado con el costo.

znewsWithScenarios3:
	2 escenarios con 1 estrategia cada uno, al comienzo ambos escenarios se satisfacen, luego de unos segundos se rompe el escenario de Response Time,
	este se arregla con la estrategia VariedReduceResponseTime, que enlista 1 servidor, esto hace que se rompa el escenario de Server Cost,
	la adaptacion lo arregla y queda el escenario de Response Time roto definitivamente.

	NOTA: El estimulo del escenario de Server Cost es ANY, por lo que no es tenido en cuenta por ScenariosManager.findBrokenScenarios(String stimulus)
	cuando el estimulo es otro (GetNewsContentClientStimulus en este caso). Aun no pude hacer que se rompa el escenario de Server Cost cuando se
	arregla el de Response Time.

znewsWithScenarios4:
	2 escenarios, el escenario de response time tiene 2 estrategias asociadas mientras que el de server cost ninguna, la estrategia
	ExpensiveReduceResponseTime repara el escenario pero rompe el de server cost, mientras que BruteReduceResponseTime lo repara
	sin romper el de server cost.