package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class BowOffenceSpecialisation extends SpecialisationBase {

    public BowOffenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new LongbowOffenceSpecialisation(this));
        addChildSpecialisation(new ShortbowOffenceSpecialisation(this));
        addChildSpecialisation(new CrossbowOffenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Bow Offence";
    }

    @Override
    public int getTier() {
        return 3;
    }

}