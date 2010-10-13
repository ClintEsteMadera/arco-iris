znewsWithScenarios1:
	Este test case sirve para mostrar el comportamiento del sistema sin auto-reparacion.
	Existe un escenario de response time solamente para poder graficar el threshold, sin embargo, al no existir estrategias, la autoreparacion
	no se disparará.

znewsWithScenarios2:
	Un escenario con 1 estrategia, al comienzo el escenario se satisface, luego de unos segundos el escenario se rompe y
	se lanza la adaptacion, escogiendo la estrategia EnlistServersResponseTime.

znewsWithScenarios3:
	2 escenarios con 1 estrategia cada uno, al comienzo ambos escenarios se satisfacen, luego de unos segundos se rompe el escenario de Response Time,
	este se arregla con la estrategia VariedReduceResponseTime, que enlista 1 o 2 servidores, esto hace que se rompa el escenario de Server Cost.
	Luego cambian las condiciones de la simulacion de manera que el response time baja y se hace posible corregir el escenario de server cost mediante
	la estrategia ReduceOverallCost.


--------
corregir de aca para abajo!

znewsWithScenarios1b:
	Un escenario con una única estrategia, al comienzo el escenario se satisface, luego de unos segundos el escenario se rompe y
	se lanza la adaptacion, escogiendo la estrategia, ejecutándola y reparando el sistema. A partir de allí el escenario
	no se rompe mas.

znewsWithScenarios4:
	2 escenarios, el escenario de response time tiene 2 estrategias asociadas mientras que el de server cost ninguna, la estrategia
	ExpensiveReduceResponseTime repara el escenario pero rompe el de server cost, mientras que BruteReduceResponseTime lo repara
	sin romper el de server cost.