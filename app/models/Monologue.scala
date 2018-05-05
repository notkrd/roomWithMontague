package models


/**
  * This class represents monologues I failed to avoid including.
  *
  * @param text The monologue itself
  * @param title An identifier
  */
class Monologue(val title: String = "", val text: String) {
}

object Monologue {
  val intro = new Monologue("Welcome to this Room", "I write to write my absence. That is, I write as an extension of other practices of trying not to be anyone. I persist and consume (images, tater-tots, tea) in this room, without any particular intent, in disingenuous avoidance of the subject position. The walls are solid and the water (only a few sips of it left at the bottom of a plastic cup) is wet. <br><br> I say this and yet continue to write, here, not like a machine. I know I might as well, ought to, speak like a machine, but writing code here the urge to intervene with something to say itches until I scratch it, as well as my knee, now, remembering I have nothing different to say about the things here except that they are, and return to a better inertness, ceasing to scratch my knee.")

  val tutorial = new Monologue("Instructions for Use","INSTRUCTIONS: Combine words to make phrases, and sentences. A sentence is true or false, or it does something more complicated, but if it is more complicated, you can work out what it does by figuring out whether some other sentence is true or false. Mostly, in this room, you compose sentences and see if they are true now, but you may also rarely be able to ask a question or issue a command. Combine phrases with phrases to make phrases, until you end up with something false or true. And then make another sentence, over again.")

  val montague = new Monologue("On genius worship","We like our mathemeticians gay, cruising clubs after the conference, strung out after the paper wandering the reflective streets to their advisor's house. Unfortunately, genius is such a hard category to shake. This argument is made in their unexpected bridge championship, or facility for gaming the stock market, or oddly expert marimba habit. I couldn't idly harness exponentials into a few Beverly Hills mansions, maintain a speed habit (Tarski, not Montague), publish teach & change the discourse's terms; the half-century late professional gossip isn't in the textbook, the journalistic summary paragraph, but the professor mentions it nonetheless, with such a tone of bemused revelation. Yet I wish I could think in other tropes, other myths, other terms. ")
  val monologues = Map("intro" -> intro, "tutorial" -> tutorial, "montague" -> montague)
}