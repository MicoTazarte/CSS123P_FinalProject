package com.ramolete.betterdialog;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuestDatabase {
    private final List<Guest> guestList = new ArrayList<>();
    private final Random random = new Random();

    // =======================
    // CONSTRUCTOR && GUEST RANDOMIZER
    // =======================
    public GuestDatabase() {
        loadGuests();
    }

    public void loadGuests() {
        Guest jayk = GuestFactory.createJayk_C3();
        Guest martin = GuestFactory.createMartin_C3();
        Guest delavega = GuestFactory.createSirDelaVega_C2();
        Guest bean = GuestFactory.createBean();
        Guest lenarianmelon = GuestFactory.createLenarianMelon_C1();
        Guest monkey = GuestFactory.createMonkey_C2();
        Guest gott = GuestFactory.createGottfrid_C3();
        Guest cube = GuestFactory.createGoldCube_C3();
        Guest supercreek = GuestFactory.createSuperCreek_C3();
        Guest madotsuki = GuestFactory.createMadotsuki_C2();
        Guest sunnyOmori = GuestFactory.createOmoriSunny_C2();
        Guest death = GuestFactory.createDeath_C2();
        Guest johnny = GuestFactory.createJohnny_C2();
        Guest joseph = GuestFactory.createJoseph_C1();
        Guest crewmate = GuestFactory.createCrewmate_C2();
        Guest hatsune = GuestFactory.createHatsuneMiku_C2();

        // RANDOMIZE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        guestList.add(martin);
        guestList.add(delavega);
        guestList.add(bean);
        guestList.add(monkey);
        guestList.add(lenarianmelon);
        guestList.add(gott);
        guestList.add(cube);
        guestList.add(supercreek);
        guestList.add(madotsuki);
        guestList.add(sunnyOmori);
        guestList.add(death);
        guestList.add(johnny);
        guestList.add(joseph);
        guestList.add(crewmate);
        guestList.add(hatsune);
        Collections.shuffle(guestList);
        guestList.add(0, jayk); // TUTORIAL. Always First.
    }

    //RANDOMIZE METHOD TO ADD GUEST !!!!!!!!!!!!!!!!!!!!

    // =======================
    // ACCESS THE GUEST LIST — FROM TOP TO BOTTOM
    // =======================

    public List<Guest> getGuestList() {
        return guestList;
    }
}
