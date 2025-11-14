package com.ramolete.betterdialog;

import java.util.Random;
import java.util.List;
import javafx.scene.image.Image;

public class GuestFactory {

    // A constant to use for image PLACEHOLDERS
    private static final String imagePathDefault = GuestFactory.class.getResource("/images/dg_p_placeholder.png").toExternalForm();
    private static final Image DEFAULT_IMAGE = new Image(imagePathDefault);
    private static final Random random = new Random();

    // ==========================================================
    // NOTE:
    // GuestFactory aims to create Guests in an organized way!
    // DIALOGCHUNKS = 1, 2, or 3
    // Sample: CHUNKS = 1: Simple Vendor (Intro -> Final Choice)
    // Requires 2 Choices.
    // Sample: CHUNKS = 2: Simple Vendor (Intro -> Secondary Choice -> Final Choice)
    // Requires 4 Choices.
    // ==========================================================

    // JAYK =====================================================
    public static Guest createJayk_C3() {

        List<String> intro = List.of(
                "Morning Sunshine! How are you doing today?",
                "It's me! Your good friend Jayk!",
                "I'm sure you remember what day it is today."
        );
        List<String> choices = List.of(
                "Remind me",  // C1.1 -> Secondary 1
                "Someone's special day",  // C1.2 -> Secondary 2
                "Who did you invite exactly?",
                "How many people did you invite?",
                "Let him in",
                "I don't trust you"
        );
        List<String> secondary1 = List.of(
                "Are you serious?",
                "I can't believe you would forget Miguel's special day...",
                "Well, anyway, here's the plan.",
                "I'm going to invite a bunch of friends Miguel has met over the years to your place.",
                "I'll warn you now: They're a colorful cast of characters, so don't be too surprised.",
                "Then we'll invite him over and give him a HUGE SURPRISE!",
                "Oh, he'll never expect it. I can just imagine the look on his face.",
                "HA HA HA HA"
        );
        List<String> secondary2 = List.of(
                "That's right! It's Miguel's birthday!",
                "You still remember the plan, right?",
                "I'm going to invite a bunch of friends Miguel has met over the years to your place.",
                "I'll warn you now: They're a colorful cast of characters, so don't be too surprised.",
                "Then we'll invite him over and give him a HUGE SURPRISE!",
                "Oh, he'll never expect it. I can just imagine the look on his face.",
                "HA HA HA HA"
        );
        List<String> third1 = List.of(
                "Who did I invite?",
                "Well, do you remember everyone you invite to your birthday?",
                "...",
                "I kind of just went down our friends list and invited everyone I saw.",
                "I just have a lot of faith in people.",
                "I'm sure that every single one of them would love to celebrate Miguel's birthday.",

                "I should mention this, though.",
                "There's been news going around about 'Concepts' suddenly appearing in this town.",
                "Creatures, or monsters... who copy the entire essence of a person. Their body, and their mind.",
                "They can turn into pretty much anything and try to infiltrate places their target always goes.",
                "However, if you try to replicate something as complex as a person, you're bound to make some mistakes.",
                "Now that's creepy.",
                "Since this is your place and all, I'll trust in your judgment.",
                "You'll decide who to let in and who to deny.",
                "How are you supposed to tell if someone's an imposter?",
                "I don't know. I'm hearing the same news you are.",
                "I'm sure it'll be obvious.",
                "With all that said...",
                "Will you let me in?"
        );
        List<String> third2 = List.of(
                "More than the fingers on my hand.",
                "So, at least more than two.",
                "Because you can't have a party without people.",
                "Looking at your place, though, I'm not sure if everyone would fit inside...",
                "But it's the only place Miguel would never expect a surprise, so please bear with me!",

                "I should mention this, though.",
                "There's been news going around about 'Concepts' suddenly appearing in this town.",
                "Creatures, or monsters... who copy the entire essence of a person. Their body, and their mind.",
                "They can turn into pretty much anything and try to infiltrate places their target always goes.",
                "However, if you try to replicate something as complex as a person, you're bound to make some mistakes.",
                "Now that's creepy.",
                "Since this is your place and all, I'll trust in your judgment.",
                "You'll decide who to let in and who to deny.",
                "How are you supposed to tell if someone's an imposter?",
                "I don't know. I'm hearing the same news you are.",
                "I'm sure it'll be obvious.",
                "With all that said...",
                "Will you let me in?"
        );
        List<String> accept = List.of(
                "See? Easy as pie.",
                "Or perhaps, cake, in this context.",
                "Trust me, this will be the GREATEST PARTY OF YOUR LIFE!",
                "I'll go in and prepare some decorations. More guests should be coming soon, so I'll leave you to door duty.",
                "Good luck!"
        );
        List<String> reject = List.of(
                "...",
                "Wow.",
                "Okay, this was unexpected.",
                "Look, I know those imposter stories are creepy and all, but there is simply no way they can replicate me—",
                "A passionate, pretentious, pragmatic individual!",
                "But, I won't judge you too harshly.",
                "Slight change of plans, I guess.",
                "I'll go and distract Miguel, while you'll invite more guests.",
                "I'll see you later..."
        );

        String imagePath = GuestFactory.class.getResource("/images/dg_p1_jayk.png").toExternalForm();
        Image bg = new Image(imagePath);

        // NOTE: dialogSecondary lists are REQUIRED, dialogThird lists are NULL.
        return new Guest(
                "Jayk", "Jayk_Tutorial", bg, "Miguel's Cake", true,
                3, // dialogChunks
                choices, intro, accept, reject,
                secondary1, secondary2, // dialogSecondary_1, dialogSecondary_2 (REQUIRED)
                third1, third2              // dialogThird_1, dialogThird_2 (NULL)
        );
    }

    // MARTIN =====================================================
    public static Guest createMartin_C3() {

        // Define ALL Dialogue Locally: MARTIN
        List<String> intro = List.of(
                "Are you having a party?",
                "Without meeeeeee?"
        );
        List<String> choices = List.of(
                "I think you're lost",
                "Who invited you?",
                "You know Miguel?",
                "We haven't met before",
                "Come in",
                "Leave"
        );
        List<String> s1 = List.of(
                "This is where Miguel's surprise party is, right?",
                "Where is the man himself?",
                "Should I even go at this point?"
        );
        List<String> s2 = List.of(
                "Your mom.",
                "Ofc I wouldn't miss that twink's birthday.",
                "We still g?"
        );
        List<String> t1 = List.of(
                "Yes, I do.",
                "OMG SAME.",
                "We have something in common.",
                "Maybe we can have a 1 on 1 League session after they all leave."
        );
        List<String> t2 = List.of(
                "Okay, and?",
                "Jayk must have set you up to this.",
                "He just invited a bunch of people you don't even know to your own house!",
                "You know, that guy pisses me off sometimes."
        );
        List<String> accept = List.of(
                "'EZ'",
                "Aight, wer is everyone?",
                "I just let them take the spotlight. You're welcome.",
                "I got him a Big M’—don't tell him.",
                "I hope he saves me a piece."
        );
        List<String> reject = List.of(
                "O shi my bad.",
                "This must be the wrong place, sorry to disturb you.",
                "'Without me...'"
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Martin", "Martin_Friend", DEFAULT_IMAGE, "Big M’", true,
                3, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                t1, t2
        );
    }

    // LENARIANMELON =====================================================
    public static Guest createLenarianMelon_C1() {

        String imagePath = GuestFactory.class.getResource("/images/dg_p2_LenarianMelon.png").toExternalForm();
        Image bg = new Image(imagePath);

        List<String> intro = List.of(
                "Hey it's me! The LenarianMelon!",
                "I am the LenarianMelon."
        );
        List<String> accept = List.of(
                "Thanks vruddy.",
                "Now I gotta check your insides! —I mean, inside your home."
        );
        List<String> reject = List.of(
                "Wowww, okay...",
                "❀࿐"
        );
        List<String> choices = List.of(
                "Let it in?",
                "Don't..."
        );

        return new Guest(
                "LenarianMelon", "YLM", bg, "Lenarian Melon", true,
                1, // dialogChunks
                choices, intro, accept, reject,
                null, null, // No secondary dialogue paths for Martin
                null, null
        );
    }

    // MONKEY =====================================================
    public static Guest createMonkey_C2() {
        int choice = random.nextInt(2);

        if (choice == 0) {
            // Option 1: Create the 'Good' Monkey
            // This assumes you have a private method or an internal constructor
            // that handles the actual building of this specific guest.
            return buildMonkeyGood();
        } else {
            // Option 2: Create the 'Bad' Monkey
            return buildMonkeyBad();
        }
    }
    private static Guest buildMonkeyGood() {

        // Define ALL Dialogue Locally: GOOD MONKEY
        List<String> intro = List.of(
                "Ooh ooh ahah.",
                "Eek eek!"
        );
        List<String> choices = List.of(
                "Umm...",
                "Is that a monkey?",
                "Let the Monkey in",
                "Deny the Monkey entry"
        );
        List<String> s1 = List.of(
                "Ooh-ooh",
                "Eep!",
                "Coo~"
        );
        List<String> s2 = List.of(
                "Ooh-ooh",
                "Eep!",
                "Coo~"
        );
        List<String> accept = List.of(
                "BANANA!",
                "(Monkey gives you a banana.)",
                "Ooh, aah!",
                "(Monkey gives a respectful bow.)"
        );
        List<String> reject = List.of(
                "Hrm...",
                "(Monkey looks sad.)",
                "Eek-eek-eek!",
                "(Monkey quietly runs away.)"
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Monkey", "Monkey (Good)", DEFAULT_IMAGE, "Banana", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // dialogSecondary_1, dialogSecondary_2 (NOT required here)
                null, null
        );
    }
    private static Guest buildMonkeyBad() {

        // Define ALL Dialogue Locally: BAD MONKEY
        List<String> intro = List.of(
                "Ahah Ooh Ooh.",
                "Rrrrawr!"
        );
        List<String> choices = List.of(
                "Umm...",
                "Is that a monkey?",
                "Let the Monkey in",
                "Deny the Monkey entry"
        );
        List<String> s1 = List.of(
                "Kek-kek",
                "Ooh-Ooh-AAAAH!",
                "Krrrr~"
        );
        List<String> s2 = List.of(
                "Kek-kek",
                "Ooh-Ooh-AAAAH!",
                "Krrrr~"
        );
        List<String> accept = List.of(
                "Eek-eek?",
                "(Monkey eyes your apartment cautiously.)",
                "Ooh!",
                "Hoo-Hoo-Hoo~HA-HAA-HAAAA!",
                "(The Monkey bursts past you, running inside to drag one of the confused guests back out of the party.)"
        );
        List<String> reject = List.of(
                "Yank-Yank-Screee!",
                "(Monkey throws a tantrum and runs away.)",
                "RRRRAWR!",
                "(Monkey breaks a nearby plant pot.)"
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Monkey", "Monkey (Bad)", DEFAULT_IMAGE, "Lose Item", false,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2,
                null, null
        );
    }

    // Sir Dela Vega =====================================================
    public static Guest createSirDelaVega_C2() {
        int choice = random.nextInt(2);

        if (choice == 0) {
            return buildSirDelaVega_GulaMelaka();
        } else {
            return buildSirDelaVega_Knowledge();
        }
    }
    public static Guest buildSirDelaVega_GulaMelaka() {

        List<String> intro = List.of(
                "Good morning.",
                "Is this the place?",
                "I overslept a bit, so I hope I'm not too late."
        );
        List<String> choices = List.of(
                "How did you find this place?",
                "Who are you?",
                "Let him in",
                "Please leave"
        );
        List<String> s1 = List.of(
                "A student of mine invited me to a party.",
                "He said that I was one of the cool professors, so I could come.",
                "I was surprised myself, but I thought I'd humor the kid for a bit, so I accepted the invitation."
        );
        List<String> s2 = List.of(
                "Right, I haven't introduced myself.",
                "My students call me 'Sir Dela Vega.' I work as a professor at a prestigious university.",
                "I teach a class on JavaFX that includes building UI, handling mouse and keyboard events, and binding properties.",
                "It's how this world was built.",
                "Ahem... I digress."
        );
        List<String> accept = List.of(
                "Thanks, I'll go inside.",
                "Here, a gift for the birthday boy.",
                "It's a delicious drink, big recommend."
        );
        List<String> reject = List.of(
                "Alright, I understand...",
                "Please tell Jayk that I came by."
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Sir Dela Vega", "Dela_Vega_Teacher_GM", DEFAULT_IMAGE, "Gula Melaka", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }
    public static Guest buildSirDelaVega_Knowledge() {

        List<String> intro = List.of(
                "Good morning.",
                "Is this the place?",
                "I overslept a bit, so I hope I'm not too late."
        );
        List<String> choices = List.of(
                "How did you find this place?",
                "Who are you?",
                "Let him in",
                "Please leave"
        );
        List<String> s1 = List.of(
                "A student of mine invited me to a party.",
                "He said that I was one of the cool professors, so I could come.",
                "I was surprised myself, but I thought I'd humor the kid for a bit, so I accepted the invitation."
        );
        List<String> s2 = List.of(
                "Right, I haven't introduced myself.",
                "My students call me 'Sir Dela Vega.' I work as a professor at a prestigious university.",
                "I teach a class on JavaFX that includes building UI, handling mouse and keyboard events, and binding properties.",
                "It's how this world was built.",
                "Ahem... I digress."
        );
        List<String> accept = List.of(
                "Thanks, I'll go inside.",
                "Here, a gift for the birthday boy.",
                "It's the greatest gift you can give to anyone."
        );
        List<String> reject = List.of(
                "Alright, I understand...",
                "Please tell Jayk that I came by."
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Sir Dela Vega", "Dela_Vega_Teacher_k", DEFAULT_IMAGE, "Knowledge", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }

    // Bean =====================================================
    public static Guest createBean() {

        List<String> intro = List.of(
                "What’s up gang, is this where we’re celebrating the birthday of my goat?"
        );
        List<String> choices = List.of(
                "How long have you known him?",
                "Tell me about Miguel",
                "Alright, come in",
                "Please leave"
        );
        List<String> s1 = List.of(
                "I've known him for a while now.",
                "We've studied together in the same school since we were teens, and we've kept in contact ever since.",
                "Everyone in that group is as different as it gets. Some are mature, some act like they're barely five, and we butt heads with each other all the time.",
                "Still, I'd like to consider them an extended family of sorts."
        );
        List<String> s2 = List.of(
                "Really? You're asking me?",
                "Bet.",
                "He's a short, quirked-up white guy with a lot of swag.",
                "He's an English major, and he looks the part.",
                "And while everyone likes to pick on him, he and everyone knows that it's our way of showing how close we are, innit."
        );
        List<String> accept = List.of(
                "Thanks bro.",
                "Here's my gift. It costs ONE MORBILLION DOLLARS.",
                "Don't lose it.",
                "I'll see you inside, innit."
        );
        List<String> reject = List.of(
                "What!?",
                "You have got to be kidding me.",
                "Your loss, buddy."
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Bean", "Bean_Friend", DEFAULT_IMAGE, "Can of Beans", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }

    // Super Creek =====================================================
    public static Guest createSuperCreek_C3() {

        // Define ALL Dialogue Locally: MARTIN
        List<String> intro = List.of(
                "Helloooooo~",
                "Anyone home?"
        );
        List<String> choices = List.of(
                "Are those horse ears?",
                "Are you human?",
                "Why are you here?",
                "Can I trust you?",
                "Sure... Come in",
                "I'll call the police"
        );
        List<String> s1 = List.of(
                "Hmm~ Never seen an Umamusume before?",
                "That's unexpected.",
                "But then again, I haven't seen any other Umamusume in this town.",
                "So I understand why you would be confused.",
                "Though, the fact that I'm the first Umamusume you've ever seen makes me blush~",
                "I hope I made a good first impression."
        );
        List<String> s2 = List.of(
                "That's rude.",
                "But I'll excuse it for now, because maybe I'm the first Umamusume you've seen.",
                "You see, I'm an Umamusume. I look just like any girl, well, except I have these ears and a tail.",
                "They say that we Umamusume are born to run. So we participate in races and do concerts when we win for our fans.",
                "I hope that clears the confusion."
        );
        List<String> t1 = List.of(
                "Hmm. Why am I here?",
                "Well, my Torena received a call one day. He said a friend of his friend was celebrating his birthday, and that I was his favorite Umamusume.",
                "Hearing that just makes me blush.",
                "Miguel... was it?",
                "I don't think he's here yet.",
                "But maybe you would like to be pampered for a while.",
                "Would you like to play pretend, and I'll be your mommy?"
        );
        List<String> t2 = List.of(
                "Oh... You still don’t trust me.",
                "You seem very anxious, but maybe I can help with that.",
                "Would you like to play a game?",
                "It’s one of my favorites. I call it...",
                "Goo-Goo Babies."
        );
        List<String> accept = List.of(
                "Thank you, my baby~",
                "I'll be sure to take good care of you.",
                "I'll pamper you so much, that all your worries will melt away.",
                "(As the tall figure enters your home, she immediately lifts you up and onto a couch. There, she coddles you and smothers you with her affection.)",
                "(It didn't feel bad, but you feel as if all this pampering is making you weaker.)"
        );
        List<String> reject = List.of(
                "Awww... It looks like you don't want me around.",
                "That breaks my heart, but if that's what you want then I'll leave.",
                "Goodbye."
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Super Creek", "Super_Creek_Uma", DEFAULT_IMAGE, "NEW CONDITION... Goo-Goo Babies", true,
                3, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                t1, t2
        );
    }

    // Gold Cube =====================================================
    public static Guest createGoldCube_C3() {

        List<String> intro = List.of(
                "...",
                "(There was a giant golden cube that manifested outside of your door.)",
                "(It appears to warp space and time itself.)",
                "(You were speechless...)",
                "(Or actually, you were speechful...)",
                "(W❒rds Sy❒b❒ls ❒mag❒❒y ❒nd ❒❒po❒s❒bl❒ G❒❒m❒❒r❒ ❒il❒ ❒❒❒r M❒n❒ ❒❒ke A❒ ❒v❒r❒l❒w❒ng ❒❒❒❒.)",
                "(❒❒❒❒ ❒❒❒❒❒ ❒❒❒❒❒❒ ❒❒ ❒❒❒❒❒ ❒❒❒❒ ❒❒❒❒❒ ❒❒❒❒ ❒❒❒❒'❒ ❒❒❒❒ ❒❒❒.)"
        );
        List<String> choices = List.of(
                "[Fear Not My Child]",
                "[For I Am Here To Protect You]",
                "[This World Is Not What It Seems]",
                "[I Can Grant You Sanctity From That Fallen Zenith]",
                "[Let Me In]",
                "(R❒s❒st...)"
        );
        List<String> s1 = List.of(
                "❒❒❒❒❒",
                "❒❒❒❒❒ ❒❒ ❒❒❒ ❒❒❒ ❒❒❒❒❒❒?",
                "❒❒❒❒❒ ❒ ❒❒❒❒ ❒❒ ❒❒ ❒❒❒❒ ❒❒❒❒❒?"
        );
        List<String> s2 = List.of(
                "❒❒❒ ❒❒❒.",
                "❒❒❒ ❒ ❒❒❒❒❒❒❒❒ ❒❒❒❒ ❒❒❒❒ ❒❒❒❒❒'❒ ❒❒❒❒❒❒❒❒.",
                "❒❒ ❒❒❒❒❒❒ ❒?"
        );
        List<String> t1 = List.of(
                "❒❒❒❒, ❒ ❒❒.",
                "❒❒❒ ❒❒❒❒.",
                "❒❒ ❒❒❒❒ ❒❒❒❒❒❒❒❒❒ ❒❒ ❒❒❒❒❒❒.",
                "❒❒❒❒❒ ❒❒ ❒❒❒ ❒❒❒❒ ❒ ❒ ❒❒ ❒ ❒❒❒❒❒❒ ❒❒❒❒❒❒❒ ❒❒❒❒❒ ❒❒❒❒ ❒❒❒ ❒❒❒❒❒."
        );
        List<String> t2 = List.of(
                "❒❒❒, ❒❒❒?",
                "❒❒❒❒ ❒❒❒❒ ❒❒❒❒ ❒❒❒ ❒❒❒ ❒❒❒ ❒❒ ❒❒❒❒.",
                "❒❒ ❒❒❒❒ ❒❒❒❒❒❒❒ ❒ ❒❒❒❒❒ ❒❒ ❒❒❒❒❒❒ ❒❒❒ ❒❒❒'❒ ❒❒❒❒ ❒❒❒❒ ❒❒ ❒❒❒❒ ❒❒❒ ❒❒❒❒❒!",
                "❒❒❒ ❒❒❒❒, ❒❒❒❒ ❒❒❒ ❒❒❒❒❒❒ ❒❒ ❒❒❒ ❒❒❒❒❒❒❒❒❒."
        );
        List<String> accept = List.of(
                "[You Have Made The Right Decision My Child]",
                "[I Shall Give You My Blessing]",
                "[All I Ask In Return Is To Lookout For My Prophet]",
                "[Gottfrid]",
                "(❒❒ ❒❒e c❒b❒ p❒a❒❒s t❒r❒❒gh yo❒r d❒❒r t❒ ent❒r y❒ur ❒ome, you begin to get control back of your mind and your body.)"
        );
        List<String> reject = List.of(
                "[❒❒❒]",
                "[You Deny Your Salvation]",
                "[The Hubris Of Man’s Folley]",
                "[But The One Who Has Control Has Made The Decision For You]",
                "[May Your Soul Find Its Peace In The Aether]",
                "(❒❒ ❒❒e c❒b❒ d❒❒s❒p❒❒es ❒n❒o ❒oth❒n❒ness, you begin to get control back of your mind and your body.)"
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Gold Cube", "Gold_Cube_G", DEFAULT_IMAGE, "Cube's Blessing", true,
                3, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                t1, t2
        );
    }

    // Gottfrid =====================================================
    public static Guest createGottfrid_C3() {

        List<String> intro = List.of(
                "Hey.",
                "This is a serious matter.",
                "Have you seen a Gold Cube around here?"
        );
        List<String> choices = List.of(
                "No...",
                "What are you talking about?",
                "What do you plan on doing with this cube?",
                "Is that all you're here for?",
                "Let him in (Give him his own corner.)",
                "You're weird, get lost"
        );
        List<String> s1 = List.of(
                "Really? That's a shame...",
                "Are you sure?",
                "Have you checked inside your house?",
                "I can help you look."
        );
        List<String> s2 = List.of(
                "The Gold Cube.",
                "It's this big [                       ].",
                "Kind of hard to miss, is it not?",
                "Or maybe you are confused.",
                "For you see, this is more than just a cube—",
                "It is my very purpose of being, my life's mission.",
                "This cube. It's...",
                "Omnipresent. Omnipotent. Omniscient.",
                "It even told me about the 'Concepts' before anyone.",
                "So I need to reach it before anyone else."
        );
        List<String> t1 = List.of(
                "What else would you do with unlimited power?",
                "First, I will create oceans of black gold to cleans the world of all its transgressions.",
                "Then, I will create propaganda for my own world.",
                "We would all be born anew, as children of this new world.",
                "And with fresh eyes, we would dance, with lovely kid band-aids and dreams!",
                "Beautiful, isn't it?",
                "That is why I need to find it. I can't do much without it."
        );
        List<String> t2 = List.of(
                "Yes?",
                "What else would I be standing outside your door for?",
                "I sense that its presence is strong here. Which is why I need to enter your home.",
                "It seems you are still hesitant to that idea for some reason.",
                "Look, if it's an offering you want, I can give it to you.",
                "The solution to all your problems. The answer to all your questions.",
                "Right here...",
                "All you have to do is let me in so I can find what I am looking for."
        );
        List<String> accept = List.of(
                "Great!",
                "Take this gift as promised. I hope it serves you well.",
                "Time to continue my search inside of your home.",
                "(He hands you a well-wrapped cylindrically shaped present. You hear a slight beeping noise inside.)",
                "(Just as he stands in his corner, the present emits loud, reverberating beats. And then...)",
                "(BOOM!)",
                "(The shock wave launched you into a wall. You were hurt, but lucky to be alive.)",
                "(No other guests seem to be hurt, and Gottfrid is still searching for that cube.)"
        );
        List<String> reject = List.of(
                "No?",
                "Well, it was worth a shot...",
                "Time to de-exist."
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Gottfrid", "Gottfrid_G", DEFAULT_IMAGE, "Pipe Bomb", true,
                3, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                t1, t2
        );
    }

    // Madotsuki =====================================================
    public static Guest createMadotsuki_C2() {
        int choice = random.nextInt(2);

        if (choice == 0) {
            return buildMadotsukiTowel();
        } else {
            return buildMadotsukiBicycle();
        }
    }
    public static Guest buildMadotsukiTowel() {

        // Define ALL Dialogue Locally: MARTIN
        List<String> intro = List.of(
                "—_—",
                "..."
        );
        List<String> choices = List.of(
                "Are you lost?",
                "Can you speak?",
                "Let her in",
                "Stay silent"
        );
        List<String> s1 = List.of(
                "Dame",
                "Muri Dame",
                "(She eyes your door, looking up and down.)"
        );
        List<String> s2 = List.of(
                "Muri",
                "Dame Dame Muri"
        );
        List<String> accept = List.of(
                "...",
                "(You hear a quick, high-frequency 'twinkle' emanating from the girl.)",
                "(She hands you a towel.)"
        );
        List<String> reject = List.of(
                "...",
                "(She pinches her cheeks, then fades away.)"
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Madotsuki", "Madotsuki_Towel", DEFAULT_IMAGE, "Towel", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }
    public static Guest buildMadotsukiBicycle() {

        // Define ALL Dialogue Locally: MARTIN
        List<String> intro = List.of(
                "—_—",
                "..."
        );
        List<String> choices = List.of(
                "Are you lost?",
                "Can you speak?",
                "Let her in",
                "Stay silent"
        );
        List<String> s1 = List.of(
                "Dame",
                "Muri Dame",
                "(She eyes your door, looking up and down.)"
        );
        List<String> s2 = List.of(
                "Muri",
                "Dame Dame Muri"
        );
        List<String> accept = List.of(
                "...",
                "(You hear a quick, high-frequency 'twinkle' emanating from the girl.)",
                "(She hands you a bicycle.)"
        );
        List<String> reject = List.of(
                "...",
                "(She pinches her cheeks, then fades away.)"
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Madotsuki", "Madotsuki_Bicycle", DEFAULT_IMAGE, "Bicycle", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }

    // Omori =====================================================
    public static Guest createOmoriSunny_C2() {
        int choice = random.nextInt(2);

        if (choice == 0) {
            return buildSunny();
        } else {
            return buildOmori();
        }
    }
    public static Guest buildSunny() {

        List<String> intro = List.of(
                "..."
        );
        List<String> choices = List.of(
                "You look a lot like Miguel",
                "Waiting for something to happen?",
                "Let him in",
                "Please leave"
        );
        List<String> s1 = List.of(
                "...",
                "(He remains silent, but he looks confused.)"
        );
        List<String> s2 = List.of(
                "...",
                "I need to tell you something."
        );
        List<String> accept = List.of(
                "...",
                "(As you open the door, the inconspicuous-looking boy pulls out a knife from his pocket!)",
                "(As the thought of getting stabbed weighs on your mind, the boy gives you the knife and walks right in as if it were nothing.)"
        );
        List<String> reject = List.of(
                "...",
                "(He looked at me as if he wanted to say something, but ended up just leaving.)"
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Sunny", "Sunny_Knife", DEFAULT_IMAGE, "Steak Knife", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }
    public static Guest buildOmori() {

        List<String> intro = List.of(
                "..."
        );
        List<String> choices = List.of(
                "Looking for something?",
                "Is that a knife?",
                "Let him in",
                "I don't want you here"
        );
        List<String> s1 = List.of(
                "...",
                "(The pale boy points his knife at your door.)"
        );
        List<String> s2 = List.of(
                "...",
                "(He nods, as he touches the point of the knife with his fingers.)"
        );
        List<String> accept = List.of(
                "...",
                "(The boy said nothing, gave you nothing, and just walked right past you.)",
                "(However, you can't forget the feeling his stare had on you. Almost like he was judging you.)"
        );
        List<String> reject = List.of(
                "...",
                "(The boy stared at the door longer; you felt his piercing gaze through it, but eventually, he left.)"
        );

        // NOTE: dialogChunks = 1 because there is only one decision point.
        return new Guest(
                "Omori", "Omori_Stare", DEFAULT_IMAGE, "Stare", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }

    // DEATH ITSELF =====================================================
    public static Guest createDeath_C2() {

        List<String> intro = List.of(
                "Hey, hello.",
                "I am Death itself.",
                "Look, I know how this looks: 'Oh no, literal Death is standing outside my door! I shouldn't let them in!'",
                "And you'd be right, but... Don't you think that's a bit prejudistic?",
                "Just because I look like this, and my job is to carry souls to the afterlife, doesn't mean I can't party in my free time.",
                "Oh, and by the way, my pronouns are 'they/them'—not because I'm genderless, but because I am a cacophony of all the souls that have come and gone. People often assume that I'm a guy."
        );
        List<String> s1 = List.of(
                "Oh heavens no!",
                "I'm not here for you, or Miguel, or any of your other guests.",
                "Darling, I was simply invited here, that's all."
        );
        List<String> s2 = List.of(
                "Why, yes.",
                "Unlike you humans, time is all we have.",
                "Right now, I'm on break, and wouldn't you know it—just in time for Miguel's party!"
        );
        List<String> accept = List.of(
                "Wait... You actually let me in!",
                "Thaaaannkkkkksss~",
                "You are a real one for not judging with prejudice.",
                "Your soul will have a nice place in the afterlife.",
                "(As Death enters your home, everyone was on edge, but he didn't do anything out of the ordinary; he just sat down on the couch.)",
                "(However, when he was accidentally touched by one of the other guests, they immediately fell to the ground.)",
                "(Death was so embarrassed, so he took the body and disappeared into the ground.)"
        );
        List<String> reject = List.of(
                "Honestly, I get it.",
                "That was awkward, even for me.",
                "Tell you what, I'm just going to skip the small talk. It's a real pity, though; you missed out on the 'Life' of party.",
                "Toodles."
        );
        List<String> choices = List.of(
                "Are you here for anyone?",
                "Death has free time?",
                "Sure, what's the worst that can happen?",
                "NO! Nope. Not letting you in"
        );

        return new Guest(
                "Death", "Dealth_Itself", DEFAULT_IMAGE, "Death", false,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }

    // Johnny =====================================================
    public static Guest createJohnny_C2() {

        List<String> intro = List.of(
                "Here's Johnny!"
        );
        List<String> s1 = List.of(
                "That is a silly question.",
                "What butcher wouldn't have a cleaver?",
                "Does it frighten you?",
                "Don't worry. I took an oath back in culinary school to only use my cleaver for its intended purpose: to chop up delicious meats and serve our patrons—",
                "For the right price, that is."
        );
        List<String> s2 = List.of(
                "Hey, wait! Why are you calling the police?",
                "YOU invited ME here!",
                "'Johnny's All Meat Catering Service'—does that ring a bell?",
                "We were called to cater a birthday event, and this was the address."
        );
        List<String> accept = List.of(
                "Perfect!",
                "You won't regret this."
        );
        List<String> reject = List.of(
                "Alright, alright!",
                "Geez, the nerve of some people..."
        );
        List<String> choices = List.of(
                "Why do you have a cleaver?",
                "I'm calling the police",
                "Fine, come in",
                "I don't buy it, leave now"
        );

        return new Guest(
                "Johnny", "Johnny_Slasher", DEFAULT_IMAGE, "Cleaver", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }

    // Joseph Joestar =====================================================
    public static Guest createJoseph_C1() {

        List<String> intro = List.of(
                "Excuse me. Is there a party in there?",
                "Sorry, buddy, but you have to let me in; there's someone I have to meet in there.",
                "I have no time to explain, but I will tell you this—",
                "My name is Joseph Joestar! Please, call me JoJo!",
                "And I can tell you exactly what your next line is.",
                "It's... 'Sure, you're cool as hell, come in!'"
        );
        List<String> accept = List.of(
                "NIIIIIIIIIIICE!"
        );
        List<String> reject = List.of(
                "Oh no, he's onto me...",
                "Time to make my leave!",
                "NIGERUNDAYO! ~ ~ ~"
        );
        List<String> choices = List.of(
                "Sure, you're cool as hell, come in!",
                "Never heard of you, leave"
        );

        return new Guest(
                "Joseph Joestar", "Jojo", DEFAULT_IMAGE, "Hamon Hairband", true,
                1, // dialogChunks
                choices, intro, accept, reject,
                null, null, // No secondary dialogue paths for Martin
                null, null
        );
    }

    // Among Us =====================================================
    public static Guest createCrewmate_C2() {
        int choice = random.nextInt(2);

        if (choice == 0) {
            return buildCrewmateG();
        } else {
            return buildCrewmateB();
        }
    }
    public static Guest buildCrewmateG() {

        List<String> intro = List.of(
                "..."
        );
        List<String> s1 = List.of(
                "..."
        );
        List<String> s2 = List.of(
                "..."
        );
        List<String> accept = List.of(
                "...",
                "(You let the strange figure in. As it entered, it turned its back to you, and you noticed a zipper running down the spine.)",
                "(You felt an impulse that it wanted you to unzip it, so you did. What came tumbling out was a person.)",
                "Uwaaaa~ Thank you!",
                "It was so stuffy in there, and when I put on the costume I couldn't get out.",
                "You're a life saver!",
                "Here, you can keep the costume. I do not want to get in it anymore."
        );
        List<String> reject = List.of(
                "...",
                "......"
        );
        List<String> choices = List.of(
                "Are you a person?",
                "Is that a costume?",
                "Let it in",
                "Leave"
        );

        return new Guest(
                "Crewmate", "Crewmate_Costume", DEFAULT_IMAGE, "Crewmate Costume", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }
    public static Guest buildCrewmateB() {

        List<String> intro = List.of(
                "..."
        );
        List<String> s1 = List.of(
                "..."
        );
        List<String> s2 = List.of(
                "..."
        );
        List<String> accept = List.of(
                "...",
                "(You let the strange figure in. When you turned your back for a second, it stabbed a person and immediately slipped away through a vent, never to be seen again.)"
        );
        List<String> reject = List.of(
                "..."
        );
        List<String> choices = List.of(
                "Are you a person?",
                "Is that a costume?",
                "Let it in",
                "Leave"
        );

        return new Guest(
                "Crewmate", "Crewmate_Monster", DEFAULT_IMAGE, "Kill", false,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }

    // Hatsune Miku =====================================================
    public static Guest createHatsuneMiku_C2() {
        int choice = random.nextInt(2);

        if (choice == 0) {
            return buildHatsuneG();
        } else {
            return buildHatsuneB();
        }
    }
    public static Guest buildHatsuneG() {

        List<String> intro = List.of(
                "Hello! It's me, Hatsune Miku.",
                "Is this where Miguel's surprise party is?"
        );
        List<String> s1 = List.of(
                "Hmm, me?",
                "Well, I was told that it was his birthday today, and that they would really appreciate it if I would come.",
                "So here I am!",
                "If it's to make someone's day unforgettable, I would be there, because I want to see everyone smile!"
        );
        List<String> s2 = List.of(
                "Hmm... am I real?",
                "Here is what I think.",
                "Whether I'm real or not should not matter to you, nor what other people think of me.",
                "But if you have felt like my music has inspired you, or has made you smile in some way, then I think I did my job properly!",
                "What matters is what you think, and how you feel.",
                "If that makes sense."
        );
        List<String> accept = List.of(
                "Really? Hooray!",
                "I promise to give an amazing performance that Miguel and all the other guests won't forget!"
        );
        List<String> reject = List.of(
                "Oh... is that so?",
                "That's okay! No matter what your reason is, I'm sure it's a good one.",
                "Please tell Miguel a happy birthday for me.",
                "Goodbye!"
        );
        List<String> choices = List.of(
                "What are you doing here?",
                "Are you real?",
                "Let her in",
                "I'm sorry, but I can't let you in"
        );

        return new Guest(
                "Hatsune Miku", "Hatsune_Miku_G", DEFAULT_IMAGE, "Concert", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }
    public static Guest buildHatsuneB() {

        List<String> intro = List.of(
                "Miku Dayooooooo~",
                "Is Miguel in there?"
        );
        List<String> s1 = List.of(
                "Of course, I'm looking for Miguel!",
                "I heard that it was his birthday today, and as a good friend of his, I was invited.",
                "Is he here yet? Where is he? I need to see him!",
                "Hey, you in there! I can hear you on the other side of the door.",
                "What are you waiting for?",
                "Let me innnnnnn~"
        );
        List<String> s2 = List.of(
                "What!? He's... not here?",
                "Or, what you meant to say is that he's not here yet? Or maybe you're just lying to me to get me to leave.",
                "Come on, you have to try harder than that; I can see right through you.",
                "A better question is...",
                "Will you let a lady freeze outside?",
                "What are you waiting for?",
                "Let me innnnnnn~"
        );
        List<String> accept = List.of(
                "Haah~ Miguel, here I comeeeeeee!",
                "(As you let the Vocaloid in, she immediately created chaos, making an enormous mess inside the party. She constructed an entire shrine dedicated to Miguel.)",
                "(You appreciated the fact that she seemed to care about Miguel, but this was too much—she was clearly obsessed.)",
                "(You tried to dismantle the shrine and clean up the damage, but you had to fight her for control of the party.)",
                "(All this fighting with the Vocaloid has left you completely drained.)"
        );
        List<String> reject = List.of(
                "what...",
                "WHAT!?",
                "You can't do this to me!",
                "LET ME IN!",
                "LET ME IN! LET ME IN! LET ME IN!",
                "LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN! LET ME IN!",
                "(The Vocaloid was screaming and kicking down the door. Thank God this door was in between me and her, keeping me away from that crazy woman!)"
        );
        List<String> choices = List.of(
                "What are you doing here?",
                "He's not here",
                "Let her in",
                "You're clearly unwell"
        );

        return new Guest(
                "Hatsune Miku", "Hatsune_Miku_B", DEFAULT_IMAGE, "Obsessed", true,
                2, // dialogChunks
                choices, intro, accept, reject,
                s1, s2, // No secondary dialogue paths for Martin
                null, null
        );
    }

}