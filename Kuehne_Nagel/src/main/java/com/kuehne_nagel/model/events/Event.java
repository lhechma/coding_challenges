package com.kuehne_nagel.model.events;

public enum Event {

    HUNDRED_METER {
        @Override
        public EventVisitor eventVisitor(EventFactory factory) {
            return factory.create100mEvent();
        }
    },
    LONG_JUMP {
        @Override
        public EventVisitor eventVisitor(EventFactory factory) {
            return factory.createLongJumpEvent();
        }
    },
    SHOT_PUT {
        @Override
        public EventVisitor eventVisitor(EventFactory factory) {
            return factory.createShotPotEvent();
        }
    },
    FOUR_HUNDRED_METER {
        @Override
        public EventVisitor eventVisitor(EventFactory factory) {
            return factory.create400mEvent();
        }
    },
    HUNDRED_TEN_METER_HURDLES {
        @Override
        public EventVisitor eventVisitor(EventFactory factory) {
            return factory.create110mHurdlesEvent();
        }
    },
    DISCUS_THROW {
        @Override
        public EventVisitor eventVisitor(EventFactory factory) {
            return factory.createDiscusThrowEvent();
        }
    },
    POLE_VAULT {
        @Override
        public EventVisitor eventVisitor(EventFactory factory) {
            return factory.createPoleVaultEvent();
        }
    },
    JAVELIN_THROW {
        @Override
        public EventVisitor eventVisitor(EventFactory factory) {
            return factory.createJavelinThrowEvent();
        }
    },
    THOUSAND_FIVE_HUNDRED_METER {
        @Override
        public EventVisitor eventVisitor(EventFactory factory) {
            return factory.create1500mEvent();
        }
    };

    public abstract EventVisitor eventVisitor(EventFactory factory);
}
