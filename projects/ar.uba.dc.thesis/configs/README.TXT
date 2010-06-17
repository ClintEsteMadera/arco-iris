znewsWithScenarios1:
	Un escenario con una única estrategia, al comienzo el escenario se satisface, luego de unos segundos el escenario se rompe y
	se lanza la adaptacion, escogiendo la estrategia, ejecutándola y reparando el sistema. A partir de allí el escenario
	no se rompe mas.

znewsWithScenarios2:
	Un escenario con 2 estrategias, al comienzo el escenario se satisface, luego de unos segundos el escenario se rompe y
	se lanza la adaptacion, escogiendo la estrategia BruteReduceResponseTime vs VariedReduceResponseTime (ambas tienen el
	mismo score ya que no existe ningún escenario relacionado con el costo.

znewsWithScenarios3:
	2 escenarios con 1 estrategia cada, al comienzo ambos escenarios se satisfacen, luego de unos segundos se rompe el escenario de Response Time,
	este se arregla con la estrategia VariedReduceResponseTime, que enlista 4 servidores, esto hace que se rompa el escenario de Server Cost,
	la adaptacion lo arregla y queda el escenario de Response Time roto definitivamente.
