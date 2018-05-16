package models


/**
  * This class represents monologues I failed to avoid including.
  *
  * @param text The monologue itself
  * @param title An identifier
  */
class Monologue(val title: String = "", val text: String) {
  override implicit def toString: String = text
}

object Monologue {
  val intro = new Monologue("Welcome to this Room", "I write to write my absence. That is, I write as an extension of other practices of trying not to be anyone. I persist and consume (images, tater-tots, tea) in this room, without any particular intent, in disingenuous avoidance of the subject position. The walls are solid and the water (only a few sips of it left at the bottom of a plastic cup) is wet. <br><br> I say this and yet continue to write, here, not like a machine. I know I might as well, ought to, speak like a machine, but writing code here the urge to intervene with something to say itches until I scratch it, as well as my knee, now, remembering I have nothing different to say about the things here except that they are, and return to a better inertness, ceasing to scratch my knee.")

  val tutorial = new Monologue("Instructions for Use","INSTRUCTIONS: Combine words to make phrases, and sentences. A sentence is true or false, or it does something more complicated, but if it is more complicated, you can work out what it does by figuring out whether some other sentence is true or false. Mostly, in this room, you compose sentences and see if they are true now, but you may also rarely be able to ask a question or issue a command. You can do the same things in that room. Combine phrases with phrases to make phrases, until you end up with something false or true. And then make another sentence, over again.")

  val montague = new Monologue("On genius worship","We like our mathematicians illegally gay, cruising clubs for a party of strangers after the conference, obsessed or strung out wandering the reflective streets to their advisor's house. Unfortunately, Genius is such a hard myth to shake (when you're into mathematics & mathematicians), the argument made in their unexpected bridge championship, or facility for gaming the stock market, or oddly expert marimba habit. I couldn't idly harness exponentials into a few Beverly Hills mansions, maintain a speed habit (Tarski, not Montague), publish teach & change the discourse's terms (one thinks, offered the mathematician's biography, embedding corresponding structures).<br><br>The half-century late professional gossip isn't in the textbook, though it makes the journalistic summary paragraph, and the professor mentions it nonetheless, with such a tone of bemused reverence. Yet one wishes to think in other tropes, other myths, other terms. Two novels will refigure this mathematician's death: Delaney deconstructive <q>pornotropic fantasy</q> <em>The Mad Men</em> and <q>a murder mystery by pop-science author and creationist hack David Berlinski</q> <em>Less Than Meets the Eye</em>. A mathematician's murder proves their theory sufficient.")

  val montague_sem = new Monologue(montague.title ++ ": semanticists", montague.text.replaceAll("mathematician(s?)", "semanticist$1"))

  val blankets = new Monologue("On blankets", "This room establishes leaf-shadowed temporalities. The intention to act is insufficient to act, even when accompanied by the ability to act. Some might respond by redefining intention to only occur when some physiological sign of action occurs. ")

  val monologues = Map("intro" -> intro, "tutorial" -> tutorial, "montague" -> montague, "montague_sem" -> montague_sem)

  val triggers = Map("the water is wet" -> intro, "the mathematician is dead" -> montague, "the semanticist is dead" -> montague_sem)
}