# Programación Concurrente

Trabajo final para materia Programación Concurrente de la carrera Ingeniería en Computación de la Universidad Nacional de Córdoba.

En la carpeta docs  se puede encontrar la descripción del trabajo. 

En resumen, se modelo con una red de Petri una planta compuesta por 4 líneas de producción que comparten tres robots entre sí. 

A partir del modelado con la red de Petri se identificarón las propiedades necesarias para modificar el sistema y que pudiera trabajar continuamente sin interrupciones. Interrupciones que podían ser causadas por el hecho de que una línea de producción podía necesitar un robot que otra línea estaba utilizando, lo que producía un bloqueo del sistema. 

Una vez solucianada esta problematica se implemento una simulación utilizando Java y los conceptos adquiridos durante el cursado sobre programación concurrente. Estos se basan en el modelado del sistema a partir de una red de Petri, lo que nos permite mantener la lógica controlada gracias a las propiedades de las redes de Petri. Tambien, se utiliza un Monitor, encargado de manejar la concurrencia de los hilos en el programa, quienes tienen la tarea de realizar las "acciones" permitidas por la red. Además, utilizamos las propiedades de la red para realizar el testeo del código.  

Todo el código se puede encontrar dentro de  src/com/petri/

Allí dentro tenemos el core del código y su correspondiente testing. 

 