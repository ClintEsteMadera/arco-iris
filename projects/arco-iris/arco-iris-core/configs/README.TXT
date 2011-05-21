znewsWithScenarios0:
	Comportamiento del Sistema sin Auto Reparación
	Este test case sirve para mostrar el comportamiento del sistema sin auto-reparacion y sin escenarios.

znewsWithScenarios1:
	Comportamiento del Sistema con un Único Escenario
	Este test case sirve para mostrar el comportamiento del sistema sin auto-reparacion.
	Existe un escenario de response time solamente para poder graficar el threshold, sin embargo, al no existir estrategias, la autoreparacion
	no se disparará.

znewsWithScenarios2:
	Comportamiento del Sistema con un Único Escenario y una Única Estrategia
	Un escenario con 1 estrategia, al comienzo el escenario se satisface, luego de unos segundos el escenario se rompe y
	se lanza la adaptacion, escogiendo la estrategia EnlistServersResponseTime.

znewsWithScenarios3:
	Tradeoff entre Escenarios
	2 escenarios con 1 estrategia cada uno, al comienzo ambos escenarios se satisfacen, luego de unos segundos se rompe el escenario de Response Time,
	este se arregla con la estrategia VariedReduceResponseTime, que enlista 1 o 2 servidores, esto hace que se rompa el escenario de Server Cost.
	Luego cambian las condiciones de la simulacion de manera que el response time baja y se hace posible corregir el escenario de server cost mediante
	la estrategia ReduceOverallCost.

znewsWithScenarios4:
	Tradeoff entre Estrategias
	2 escenarios, el escenario de response time tiene 2 estrategias asociadas mientras que el de server cost ninguna, las estrategias
	ExpensiveReduceResponseTime y LowerFidelityReduceResponseTime compiten entre sí, ganando la segunda ya que la primera rompe el 
	escenario de costo.

znewsWithScenarios5:
	Se define por Concern.
	2 escenarios con la misma prioridad, uno de costo y otro de tiempo de respuesta, se define por peso del concern 
	asignado en el environment actual (NORMAL)