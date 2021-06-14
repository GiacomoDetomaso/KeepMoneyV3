# KeepMoney: nomi componenti grafiche

## Regole per i nomi degli oggetti

Per quanto riguarda i nomi all'interno del file XML, devono seguire questo format: (prefisso)(nome)(opzionale: suffisso)

- Il prefisso è un abbreviazione del tipo di oggetto  (per esempio testo editabile txt, Button btn...)

- Il nome è l'identificativo dell'oggetto grafico

- In caso di omonimia, con componenti di altre activity, inserire un suffisso che corrisponde all'iniziale dell'actvity di riferimento

Gli oggetti Java relativi agli oggetti grafici XML devono mantenere lo stesso nome. Si recuperano con la funzione findViewById(R.id.nomeOggetto)

## Regole per i nomi di Activity, Fragment e layout

Per quanto riguarda i nomi relativi delle Activity, Fragment o Dialog la regola è quella di default: 

- Le classi Java hanno questo formato (Tipo)(Nome): dove il tipo è Activity, Fragment ecc..

- I relativi File di layout hanno questo formato: (Nome)_(tipo)


