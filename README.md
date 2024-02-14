# IMDB

Nume: **Ciucan Andrei-Alexandru**

Grupa: 323CC

Gradul de dificultate al temei: mediu

Timpul alocat rezolvarii: 1,5 saptamani

## MODUL DE IMPLEMENTARE: 

* Mediul ales: Intellij IDEA



* Am implementat urmatoarele design pattern-uri: **Singleton** (clasa IMDB), **Factory** (clasa UserFactory), **Builder** (pentru clasa statica Information, interna lui User), **Observer** (interfata Observer si Subject este implementata de clasa User, subiectele sunt reprezentate de cereri si rating-uri, iar observatorii sunt userii), **Strategy** (actualizarea experientei utilizatorilor de tip regular și contributor).

* Aplicatia dispune de doua moduri de folosinta, in terminal, dar si in interfata grafica.

* Tot ce se ruleaza in linia de comanda foloseste metode preponderent din clasa IMDB.

* Interfata grafica realizata in Swing, consta intr-o fereastra de login, dupa care userul va fi redirectionat spre o noua fereastra unde va avea un meniu. In functie de tipul userului meniul difera. Aici poate alege sa faca mai multe actiuni, cum ar fi vizualizarea productiilor si actorilor din sistem etc. Pentru fiecare fereastra nou deschisa am creat clase speciale in directia aceasta, urmarind de asemenea realizarea unui cod cat mai usor de urmarit.

* `IMDB`: in aceasta clasa prin intermediul metodei run() care este apelata in main se declanseaza executia intregii aplicatii.

* `JSONParser` este clasa raspunzatoare de incarcarea datelor in sistem (din fisierele Json).

* `JSONWriter` este clasa raspunzatoare de salvarea datelor dupa incheierea executiei aplicatiei. Data viitoare cand va fi lansata aplicatia, vor fi incarcate datele salvate anterior.