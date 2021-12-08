# Diseño

En este documento se describe el proceso de analisis del problema y el diseño realizado.

## Funcionalidad de maximización de uso del cupón

En primera instancia se considera el problema de optimización que busca maximizar el total gastado con limite en el valor total del cupón, donde el item _i_ con precio _p<sub>i</sub>_ del conjunto de _N_ items, se puede incluir o no: _x<sub>i</sub> = {0,1}_, por lo cual esta se busca optimizar la siguiente expresión:

<img src="https://latex.codecogs.com/png.image?\dpi{100}&space;\bg_white&space;max\(\sum_{i=1}^{N}&space;p_i&space;x_i\),&space;\sum_{i=1}^{N}&space;p_i&space;x_i&space;\leq&space;total,&space;x_i&space;\in&space;\{0,1\}" title="\bg_white max\(\sum_{i=1}^{N} p_i x_i\), \sum_{i=1}^{N} p_i x_i \leq total, x_i \in \{0,1\}" />

A partir de lo cual se puede asociar este problema como una variante del problema de la mochila (_knapsack problem_ en inglés), donde el cálculo de todas las posibles complejidad de. Por una parte, teniendo en consideración que los usuarios pueden tener una cantidad significativa de items como favoritos, de acuerdo a una consideración descrita posteriormente en el enunciado del requerimiento "Hay usuarios que tienen miles de items en favoritos". Y por otra parte, la cantidad de items que permite el cupon es significativamente menor en comparación al número de posibles candidatos. Por lo cual, para estos casos el espacio de búsqueda es mucho mayor al necesario para obtener la solución, y en consecuencia, se plantea el uso de un algoritmo hibrido combinando programación dinamica Top-Down, y ramificación y poda.

