///////////////////////////////////
//Listing 1: SteinPapierSchere.kt//
///////////////////////////////////
package de.e2.sps.start

enum class Wahl {
    STEIN, PAPIER, SCHERE
}

enum class Ergebnis {
    SPIELER_GEWINNT, COMPUTER_GEWINNT, UNENTSCHIEDEN
}

class Spiel(val spielerWahl: Wahl, val computerWahl: Wahl) {
    fun ermittleErgebnis(): Ergebnis {
        return when {
            spielerWahl == computerWahl -> UNENTSCHIEDEN
            spielerWahl == PAPIER && computerWahl == STEIN -> SPIELER_GEWINNT
            spielerWahl == STEIN && computerWahl == SCHERE -> SPIELER_GEWINNT
            spielerWahl == SCHERE && computerWahl == PAPIER -> SPIELER_GEWINNT
            else -> COMPUTER_GEWINNT
        }
    }
}


////////////////////////////////////////////////
//Listing 2: Mit main die Spielmechanik testen//
////////////////////////////////////////////////
fun main(): Unit {
    val spiel = Spiel(STEIN, PAPIER)
    val spielerWahl = spiel.spielerWahl
    println("Spieler: $spielerWahl. Computer: ${spiel.computerWahl}.")
    println("Ergebnis: ${spiel.ermittleErgebnis()}.")
}


///////////////////////////////////////////
//Listing 3: Comparable<T> implementieren//
///////////////////////////////////////////
data class Rechteck(val breite: Int, val hoehe: Int): Comparable<Rechteck> {
    val flaeche: Int = breite * hoehe

    override fun compareTo(other: Rechteck): Int = this.flaeche - other.flaeche
}

fun main() {
    val r1 = Rechteck(10, 20)
    val r2 = r1.copy(hoehe = 30)
    println(r2)       // Rechteck(breite=10, hoehe=30)
    println(r1 == r2) // false
}


////////////////////////////////////////////////
//Listing 4: repeat – Funktionen als Parameter//
////////////////////////////////////////////////
fun repeat(times: Int, action: (Int) -> Unit) { 
    
    for (index in 1..times) { 
            action(index)
        }
    } 
    fun printNumber(number: Int) {
        println(number)
    } 
    fun main() {
        repeat(3, ::printNumber)
    }


////////////////////////////
//Listing 5: Lambda mit it//
////////////////////////////
fun main() {repeat(3) { number -> 
    println(number)
}
repeat(3) {
    println(it)
} }


///////////////////////////////////
//Listing 6: Hello World mit Ktor//
///////////////////////////////////
fun main() {
    println("Open with: http://localhost:8080/hello?count=3")
    val server = embeddedServer(factory = Netty, port = 8080) {
        //this ist Application
        routing { 
            //this ist Routing
            get("/hello") {
                ...
            }
        }
    }
    server.start(wait = true)
}


////////////////////////////
//Listing 7: countAsString//
////////////////////////////
get("/hello") { 
    //this ist PipelineContext
    val countAsString: String? = call.parameters["count"]
    val count = if (countAsString!=null) countAsString.toInt() else 3
    call.respondTextWriter { 
        //this ist Writer
        repeat(count) {
            write("Hello World\n")
        }
    }
}


/////////////////////////////////////////////
//Listing 8: Strings mit toWahl() erweitern//
/////////////////////////////////////////////
fun String.toWahl() = //this ist String
    when (lowerCase()) {
        "stein" -> STEIN
        "papier" -> PAPIER
        "schere" -> SCHERE
        else -> null
    }


////////////////////////////////////////
//Listing 9: WebHandler implementieren//
////////////////////////////////////////
get("/spiel") {
    val spielerWahl = call.parameters["wahl"]?.toWahl()
    if (spielerWahl == null) {
        call.respondText("Ungültige Wahl!")
    } else {
        val computerWahl = Wahl.values().random()
        val spiel = Spiel(spielerWahl, computerWahl)

        call.respondText(
            """
            <h1>Stein Papier Schere</h1>    
            <p>Spieler hat gewählt ${spiel.spielerWahl}.</p>
            <p>Computer hat gewählt ${spiel.computerWahl}.</p>
            <p>Das Ergebnis ist: ${spiel.ermittleErgebnis()}.</p>
            """.trimIndent(), ContentType.Text.Html
        )
    }
}


////////////////////////////
//Listing 10: kotlinx.html//
////////////////////////////
get("/start") {
    call.respondHtml {
        body {
            h1 { text("Stein Papier Schere") }
            form(action = "/spiel", method = FormMethod.get) {
                // this ist FORM
                label { +"Deine Wahl:" }
                wahlButton("Stein")
                wahlButton("Papier")
                wahlButton("Schere")
            }
        }
    }
}

fun FORM.wahlButton(wahl: String) {
    button(type = ButtonType.submit, name = "wahl") {
        value = wahl.lowerCase()
        +wahl
    }
}