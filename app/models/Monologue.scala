package models

import scala.util.Random


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


  val intro = new Monologue("Welcome to this Room", "I write to write my absence. That is, I write as an extension of other practices of trying not to be anyone. I persist and consume (images, tater-tots, tea) in this room, without any particular intent, in disingenuous avoidance of the subject position. The walls are solid and the water (only a few sips of it left at the bottom of a plastic cup) is wet. <br><br> I say this and yet continue to write, here, not like a machine. I know I might as well, ought to, speak like a machine, but writing code here the urge to intervene with something to say itches until I scratch it, as well as my knee, now, remembering I have nothing different to say about the things here except that they are, and return to a better inertness, under the dirty blanket, ceasing to scratch my knee.")

  val color = new Monologue("Lyric is a dry throat", "If this place is or was there might be a color of it. One light in the corner above the bed scalds, casts purple after-images on, idle eyes. The blind’s reflection and a hint of the rotten fruit underline a threat of something changing irresistibly. I sneeze with the dust, but it’s too cold out to friction the window open - if I do I’ll need to surround myself in the printed cotton blanket again, sweat-and-rust scented, scattered with crumbs to be indefinitely pinched away - better to sneeze. Where, rather, metaphor is is steeped in frost and footfalls. Somehow the body continues: lyric is a dry throat.")

  val metaphor = new Monologue("On stomach acid", "It seems that this narrates. The weight of a stomach acidic, dust endangering the beautiful bright screen. Between bile and circulation an agent selects one world among others to pursue. But the system works itself out currently, diodes activated then fading in the dust-welcoming gaps beneath keys. Writers are fired for picking the wrong adjectives, after all. The spongy ground sweats orange.")

  val tutorial = new Monologue("Instructions for Use","INSTRUCTIONS: Combine words to make phrases, and sentences. A word is to be clicked on, unless it is red, in which case it is not permitted (if you intend to follow The Rules). A sentence is true or false, or it does something more complicated, but if it is more complicated, you can work out what it does by figuring out whether some other sentence is true or false. Mostly, in this room, you compose sentences and see if they are true now, but you may also rarely be able to ask a question or issue a command. You can do the same things in that room. So: combine phrases with phrases to make phrases, until you end up with something false or true. And then make another sentence, over again.")

  val montague = new Monologue("This Dead Logician","We like our mathematicians illegally gay, cruising clubs for a soiree of strangers (<q>some kind of soiree</q> write the Fefermans) after the conference, out of mind wandering the reflective streets to their advisor's house. Two novels will refigure this mathematician's death: Delaney's deconstructive <q>pornotropic fantasy</q> <em>The Mad Men</em> and <q>a murder mystery by pop-science author and creationist hack</q> David Berlinski <em>Less Than Meets the Eye</em>. A mathematician's murder proves their theory sufficient; the silence of the work its beauty, some handful of facts in his advisor's student's biography oddly conducive to porn and hack thrillers. ")

  val genius = new Monologue("On genius worship", "Unfortunately, the Genius myth is such a hard habit to shake for mathematicians, the argument made in their unexpected bridge championship, oddly expert organ playing, wealth made as an afterthought. I couldn't idly harness exponentials into a few Beverly Hills mansions, maintain a speed habit (Tarski, not Montague), publish teach & change the discourse's terms (one thinks, offered the mathematician's biography, embedding corresponding structures; <q>I did work it out when I was 13<q> quoth Tarski).<br><br>The half-century late professional gossip isn't in the textbook, though it makes the journalistic summary paragraph, and the professor mentions it nonetheless, with such a tone of bemused revelation. Yet one wishes to think in other tropes, other myths, other terms.")

  val montague_sem = new Monologue(montague.title ++ ": semanticists", montague.text.replaceAll("mathematician(s?)", "semanticist$1"))

  val montague_org = new Monologue(montague.title ++ ": organists", montague.text.replaceAll("mathematician(s?)", "organist$1"))

  val montague_inv = new Monologue(montague.title ++ ": successful real estate investor", montague.text.replaceAll("mathematician(s?)", "successful real estate investor$1"))

  val blankets = new Monologue("On blankets", "This room establishes leaf-shadowed temporalities. The intention to act is insufficient to act, even when accompanied by the ability to act. Some might respond by redefining intention to only occur when some physiological sign of its effect becomes measured; I turn towards the bright screen.")

  val blood = new Monologue("Bloody water", "The body's water drains into the hole in the floor, after filling the towel round Montague's neck. This blood is imagined; the book left it out. But the water is wet and a body must leak.")

  val thx = new Monologue("Thank you very much", "I'm alive, thank you very much, and I left math 17 months ago.")

  val glows = new Monologue("A Glowing Screen", "It reads: [**]")

  val delaney = new Monologue("Delaney quotes", "<blockquote><q>There is no articulate resonance. The common problem, I suppose, is to have more to say than vocabulary and syntax can bear. That is why I am hunting in these desiccated streets. The smoke hides the sky's variety, stains consciousness, covers the holocaust with something safe and insubstantial. It protects from greater flame. It indicates fire, but obscures the source. This is not a useful city. Very little here approaches any eidolon of the beautiful.</q><br><br>-Samuel Delaney, <em>Dhalgren</em></blockquote>")

  val wet = new Monologue("The mathematician is wet", "How wetness too can be of different kinds; the wetware implements certain inexplicable diversions. The Fefermans, in their biography of Tarski - a cumulative page and a half of which is the primary public biographical resource on Montague - indicate somewhere, well after Montague's death in 1971, near Tarski's death in 1983, Tarski calling Montague one of only two mathematicians Tarski enjoyed spending time with. The Fefermans, both Tarski's students, characterize this a polite lie. Tarski happened to board the last ship to leave Poland for the US, in September 1939, for a positivism-inspired Unity of Science conference, on which boat he happened to meet a student of Quine's. Quine, among less famous others, arranged for Tarski to remain in Harvard, by the River Charles. Indianapolis is the only large city in the US not on a body of water.")

  val knots = new Monologue("Various Knots", "I imagine various knots. This is a dark turn of mind. One must consider the semantics of holes. It is possible that the strangling towel made only an unknot.")

  val window = new Monologue("Window light", "The slat blind sits halfway down the window, above the cups and so on. <q>All I need is a window not to write</q>, or something like that, the window extraneous.")

  val langs = new Monologue("Rather than write", "Rather than write a new paragraph, I follow tutorials to write a program that prints \"Hello, Darkness!\" in a new language: Elm, Rust, Kotlin, Clojure - if you let the web frameworks in Yesod, Figwheel, and Snap too. Why so many machines to say the same nothing. What machine might be sufficient. When might something else in the math happen, resonant: one is drawn to the colors of the encoding light, structures of call and response. You write into '>' and some echo instantly rebounds or breaks, as though some turn of myself said so back to myself, knew how to say it. ")

  val itself = new Monologue("Itself", "Lying there, in search of a way of talking about nothing except the conditions of its own possibility. A suggestion is that there is only one other thing to do with it. (The hint is given because there is a quote there, that I like, want you to read.")

  val soap = new Monologue("Laundering", "One might say that it is being laundered. One might also say that this makes no sense if the killing's end was monetary theft. One might even speculate that leaving a wallet out, full, would be a very clever and convincing ruse if this were some more subtle burglary (<q>would</q> taking Saul Kripke to say something calculable about, those speculative burglars long gone (admittedly, Saul wrote first, but the math's subtlety makes his tuples taught second; and anyway, that mathematician survives)).")

  val windowscreen = new Monologue("Window screen", "The window's screen reveals half a shed, some trees. ")

  val unknowns = new Monologue("I write what I do not understand","What I write about that room is what I do not know about it. I only guess an ant. Wallpapered or painted, marble or parquet, where the water and windows, the distribution of blood and water. The soap dish left becomes a clue, emitting terrible significance, as I continue to write what I do not understand, from what some others (those Fefermans) may have known more about but did not write. ")

  val flip = new Monologue("flip", "Flip it.")

  val contradiction = new Monologue("The thread of it", "The thread of a story leads to some terrible transparency, an inversion of the night. Detectives catch their targets, are caught by them, or turn away from something terrible, out in the streets. The sentence, even this, turns after many diversions, divisions, gatherings, towards one verdict, a crossing by the river. Stay a while, won't you, before the parking lot's return.")

  val absence = new Monologue("What this room does not have", "The air around it inevitable, thickening. The white of memory, imagined memory, more palpable, of course.  ")

  val mansion = new Monologue("Mansion", "This room is a room in a mansion. This has consequences, makes suggestions, for the room here, none of them essential. Towels might be plush, a bath marble, the sink and counter and hangers extended. Somewhere we are told of a flamboyance, making more possible plush towels and marble. ")

  val organs = new Monologue("What this room does not have", "I am not a detective. I do not establish new truths. I am not a search engine. I am not a calculator. I do not know the color of Montague's hair or eyes. But I know he played the organ very well, at a church he did not believe in. ")

  val unspeakables = new Monologue("Bad", "Let us say something very bad, inexcuseable, about our genius. A few sentences (twice) assert Montague had sex with a minor, and was put on trial for it. Some things speak themselves. It was allegedly consensual, but we needn't rationalize pedestals. Letters from Tarski and other philosophers were submitted to the court, which released him, Berkeley reinstating his job. Probably there are records somewhere, as the courts produce. I do not know what to say about this further, it shall not explicitly cause additional swerves. The footnote is the story, and explains, paraphrasably, nothing at all.")

  val choices = new Monologue("choices", "I have a choice, in ‘telling Montague’s story.’ What it means when someone says ‘I have a choice’ is that the speaker identifies several possible actions in some situation, and they have decided on one, but they do not think their decision easy or trivial, and indeed many spineless others would have followed another course. I am not going to do the second part of this. The first is to cross, somehow or more or differently, a line into historical Fiction. Then I may tell a detective story, write the bedroom, invent an unrecorded confidant. The second would be some kind of research: I could actually read his published papers; I could find a news article or obituary, I could speak to someone who knew him in Berkeley or LA. Instead I will stop, write the interruption.")

  val submerged = new Monologue("submerged books", "As though talking underwater. Immersion as film trope of depression, or more specifically, the distortion, refraction, of suburban life. Slow static, washes of aural correction, re-mediate public sound. I hum while swimming")

  val wallet_life = new Monologue("living money","Logic claims a position of remove from material conditions while fighting to mediate them, of course.")

  def on_screen(): Monologue = {
    val randomness = new Random(System.currentTimeMillis)
    val txt_src = randomness.shuffle(List("kant", "plato")).head
    txt_src match {
      case "kant" => from_kant()
      case "plato" => from_plato()
    }
  }

  def from_kant(): Monologue = {
    val randomness = new Random(System.currentTimeMillis)
    val start_index = randomness.nextInt(sampleTexts.kant_chain.length - 501)
    new Monologue("On a screen", s"It reads: [*${sampleTexts.kant_chain.slice(start_index, start_index + 500)}*]")
  }

  def from_plato(): Monologue = {
    val randomness = new Random(System.currentTimeMillis)
    val start_index = randomness.nextInt(sampleTexts.plato_chain.length - 501)
    new Monologue("On a screen", s"It reads: [*${sampleTexts.plato_chain.slice(start_index, start_index + 500)}*]")
  }

}