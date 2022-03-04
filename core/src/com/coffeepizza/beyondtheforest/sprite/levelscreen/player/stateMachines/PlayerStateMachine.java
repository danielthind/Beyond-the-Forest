package com.coffeepizza.beyondtheforest.sprite.levelscreen.player.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.player.Player;

public enum PlayerStateMachine implements State<Player> {

    NEUTRAL() {
        @Override
        public void enter(Player entity) {
            entity.zeroXVel();
        }

        @Override
        public void update(Player entity) {
            entity.zeroXVel();
        }
    },
    FROZEN() {
        /** Used for Bouncers */
        @Override
        public void enter(Player entity) {
            entity.zeroXVel();
        }

        @Override
        public void update(Player entity) {
            if (entity.isGroundedNormal()) {
                if (entity.jumpJustPressed()) {
                    entity.savedRunningRight = entity.runningRight;
                    entity.stateMachine.changeState(PlayerStateMachine.JUMP_CHARGE);
                } else if (entity.leftPressed()) {
                    if (entity.runningRight) { entity.changeDirection(); }
                } else if (entity.rightPressed()) {
                    if (!entity.runningRight) { entity.changeDirection(); }
                } else if (entity.changeDirJustPressed()) {
                    entity.changeDirection();
                }
                /*
                else if (entity.controlsLRInsteadOfCD()) {
                    if (entity.runningRight && entity.leftPressed()) {
                        entity.changeDirection();
                    }
                    if (!entity.runningRight && entity.rightPressed()) {
                        entity.changeDirection();
                    }
                } else if (!entity.controlsLRInsteadOfCD()) {
                    if (entity.changeDirJustPressed()) {
                        entity.changeDirection();
                    }
                }
                */
            }// else {
            //    entity.stateMachine.changeState(PlayerStateMachine.JUMP_DESCEND);
            //}
        }

        @Override
        public void exit(Player entity) {
            entity.isFrozen = false;
        }
    },
    SLEEP() {
        @Override
        public void enter(Player entity) {
            entity.halfVelocities();
        }

        @Override
        public void update(Player entity) {
            entity.halfXVel();
        }
    },
    GROWL() {
        @Override
        public void enter(Player entity) {
            entity.halfVelocities();
        }

        @Override
        public void update(Player entity) {
            /** TODO */
        }
    },
    RUN() {
        @Override
        public void enter(Player entity) {
            entity.runPhysics();

        }

        @Override
        public void update(Player entity) {
            if (entity.isGroundedNormal()) {
                /** Inputs */
                if(entity.jumpJustPressed()) {
                    entity.savedRunningRight = entity.runningRight;
                    entity.stateMachine.changeState(PlayerStateMachine.JUMP_CHARGE);
                } else if (entity.attackJustPressed()) {
                    entity.stateMachine.changeState(PlayerStateMachine.ATTACK_DASH);
                } else if (entity.changeDirJustPressed()) {
                    entity.changeDirection();
                } else if (entity.leftPressed()) {
                    if (entity.runningRight) {
                        entity.changeDirection();
                    }
                } else if (entity.rightPressed()) {
                    if (!entity.runningRight) {
                        entity.changeDirection();
                    }
                }

                /** Autorunning */
                //else
                if (entity.autoRun) {
                    entity.runPhysics();
                }/* else {
                    if (entity.leftPressed()) {
                        entity.runningRight = false;
                        entity.runPhysics();
                    } else if (entity.rightPressed()) {
                        entity.runningRight = true;
                        entity.runPhysics();
                    }
                }*/

            } else {
                entity.stateMachine.changeState(PlayerStateMachine.JUMP_DESCEND);
            }
        }
        @Override
        public void exit(Player entity) {
            //entity.getActorBody().setGravityScale(1);

//            if (entity.getRayNormal().y != 1) {
//                    mn.y != 1) {
//                if (entity.getActorBody().getLinearVelocity().y < 0.000005f && entity.getActorBody().getLinearVelocity().y > -0.000005f) {
                    //Gdx.app.log("PlayerStateMachine", "Run Exit");
//                }
//            }
        }
    },
    JUMP_CHARGE() {
        @Override
        public void enter(Player entity) {
            entity.halfXVel();
            entity.initJumpChargeOrLandingPhysics(true);
        }

        @Override
        public void update(Player entity) {
            entity.halfXVel();
            if (entity.freeFromLockout()) {
                entity.stateMachine.changeState(PlayerStateMachine.JUMP_ASCEND);
            }
        }
    },
    JUMP_ASCEND() {
        @Override
        public void enter(Player entity) {
            entity.initJumpPhysics();
        }

        @Override
        public void update(Player entity) {
            if (entity.freeFromLockout()) {
                // Lockout before isGrounded so prevent early Landing
                if (entity.isGroundedNormal()) {

                    /**
                     * DEBUGGING
                     */
                    //entity.logRayDetails();
                    /**
                     * DEBUGGING
                     */

                    entity.stateMachine.changeState(PlayerStateMachine.JUMP_LANDING);
                } else {
                    if (entity.jumpJustPressed()) {

                        /** Advanced Controls */
                        if (entity.currentInputLevel == Player.InputLevel.ADVANCED) {
                            entity.stateMachine.changeState(PlayerStateMachine.DOUBLE_JUMP_ASCEND);
                        }
                    } else if (entity.attackJustPressed()) {

                        /** Intermediate or Advanced Controls */
                        if (entity.currentInputLevel == Player.InputLevel.INTERMEDIATE
                                || entity.currentInputLevel == Player.InputLevel.ADVANCED) {
                            entity.stateMachine.changeState(PlayerStateMachine.ATTACK_DIVE);
                        }

                    } else if (entity.isDecending()) {
                        entity.stateMachine.changeState(PlayerStateMachine.JUMP_DESCEND);
                    }
                }

                // Continue jump arc (fix a short object in your x dir zeroing your x velocity even when you jump above it)
                entity.continueJumpPhysics();
            }
        }
    },
    JUMP_DESCEND() {
        @Override
        public void enter (Player entity) {
            entity.initFallPhysics();
        }

        @Override
        public void update(Player entity) {
            // For physics issue
            entity.checkUnstuck();

            // Normal logic
            if (entity.isGroundedNormal()) {
                entity.stateMachine.changeState(PlayerStateMachine.RUN);
            } else {
                if (entity.jumpJustPressed()) {
                    /** Advanced Controls */
                    if (entity.currentInputLevel == Player.InputLevel.ADVANCED) {
                        entity.stateMachine.changeState(PlayerStateMachine.DOUBLE_JUMP_ASCEND);
                    }
                } else if (entity.attackJustPressed()) {

                    /** Intermediate or Advanced Controls */
                    if (entity.currentInputLevel == Player.InputLevel.INTERMEDIATE
                            || entity.currentInputLevel == Player.InputLevel.ADVANCED) {
                        entity.stateMachine.changeState(PlayerStateMachine.ATTACK_DIVE);
                    }
                } else {
                    entity.limitDescendVelocity();
                }
            }
        }
    },
    DOUBLE_JUMP_ASCEND() {
        @Override
        public void enter(Player entity) {
            entity.initJumpPhysics();
        }
        @Override
        public void update(Player entity) {
            if (entity.isDecending()) {
                entity.stateMachine.changeState(PlayerStateMachine.DOUBLE_JUMP_DESCEND);
            } else if (entity.attackJustPressed()) {

                /** Intermediate or Advanced Controls */
                if (entity.currentInputLevel == Player.InputLevel.INTERMEDIATE
                        || entity.currentInputLevel == Player.InputLevel.ADVANCED) {
                    entity.stateMachine.changeState(PlayerStateMachine.ATTACK_DIVE);
                }
            }

            // Continue jump arc (fix a short object in your x dir zeroing your x velocity even when you jump above it)
            entity.continueJumpPhysics();
        }

    },
    DOUBLE_JUMP_DESCEND() {
        @Override
        public void update(Player entity) {
            if (entity.isGroundedNormal()) {
                entity.stateMachine.changeState(PlayerStateMachine.JUMP_LANDING);
            } else if (entity.attackJustPressed()) {

                /** Intermediate or Advanced Controls */
                if (entity.currentInputLevel == Player.InputLevel.INTERMEDIATE
                        || entity.currentInputLevel == Player.InputLevel.ADVANCED) {
                    entity.stateMachine.changeState(PlayerStateMachine.ATTACK_DIVE);
                }
            }
        }
    },
    JUMP_LANDING() {
        @Override
        public void enter(Player entity) {
            //entity.stateMachine.changeState(PlayerStateMachine.RUN);
            entity.halfVelocities();
            entity.initJumpChargeOrLandingPhysics(false);
        }
        @Override
        public void update(Player entity) {
            if (entity.freeFromLockout()) {
                if (entity.isGroundedNormal()) {
                    entity.stateMachine.changeState(PlayerStateMachine.RUN);
                } else {
                    entity.stateMachine.changeState(PlayerStateMachine.JUMP_DESCEND);
                }
            }
        }
    },
    ATTACK_DASH() {
        @Override
        public void enter(Player entity) {
            entity.slidePhysics(true);
            entity.attacking = true;
        }
        @Override
        public void update(Player entity) {
            if (entity.isGroundedExtended()) {
                if(entity.jumpJustPressed()) {
                    /** Intermediate or Advanced Controls */
                    if (entity.currentInputLevel == Player.InputLevel.INTERMEDIATE
                            || entity.currentInputLevel == Player.InputLevel.ADVANCED) {
                        entity.stateMachine.changeState(PlayerStateMachine.LONGJUMP_ASCEND);
                    }
                } else if (entity.attackJustPressed()) {
                    /** Advanced Controls */
                    if (entity.currentInputLevel == Player.InputLevel.ADVANCED) {
                        entity.stateMachine.changeState(PlayerStateMachine.ATTACK_JUMP_ASCEND);
                    }
                } else if (entity.freeFromLockout()) {
                    entity.stateMachine.changeState(PlayerStateMachine.RUN);
                } else {
                    entity.slidePhysics(false);
                }
            } else {
                entity.stateMachine.changeState(PlayerStateMachine.JUMP_DESCEND);
            }
        }

        @Override
        public void exit (Player entity) {
            entity.attacking = false;
        }
    },
    LONGJUMP_ASCEND() {
        @Override
        public void enter(Player entity) {
            entity.initLongJumpPhysics();
        }
        @Override
        public void update(Player entity) {
            if (entity.isDecending()) {
                entity.stateMachine.changeState(PlayerStateMachine.DOUBLE_JUMP_DESCEND);
            } else if (entity.attackJustPressed()) {

                /** Intermediate or Advanced Controls */
                if (entity.currentInputLevel == Player.InputLevel.INTERMEDIATE
                        || entity.currentInputLevel == Player.InputLevel.ADVANCED) {
                    entity.stateMachine.changeState(PlayerStateMachine.ATTACK_DIVE);
                }

            }
        }
    },
    ATTACK_JUMP_ASCEND() {
        @Override
        public void enter(Player entity) {
            entity.slideKickPhysics();
            entity.attacking = true;
        }

        @Override
        public void update(Player entity) {
            if (entity.freeFromLockout()) {
                entity.stateMachine.changeState(PlayerStateMachine.ATTACK_JUMP_DESCEND);
            }

            /*
            if (entity.freeFromLockout()) {

                if (entity.isGroundedNormal()) {
                    entity.stateMachine.changeState(PlayerStateMachine.RUN);
                } else {
                    entity.stateMachine.changeState(PlayerStateMachine.JUMP_DESCEND);
                }

            }
            */
        }
    },
    ATTACK_JUMP_DESCEND() {
        @Override
        public void enter(Player entity) {
            entity.setLockout(entity.halfSlideKickTimer);
        }

        @Override
        public void update(Player entity) {
            if (entity.isGroundedNormal()) {
                entity.stateMachine.changeState(PlayerStateMachine.RUN);
            } else {
                if (entity.freeFromLockout()) {
                    entity.stateMachine.changeState(PlayerStateMachine.JUMP_DESCEND);
                }
            }
        }

        @Override
        public void exit (Player entity) {
            entity.attacking = false;
        }
    },
    /*
    ATTACK_JUMP_LANDING() {
        @Override
        public void enter(Player entity) {
            entity.initJumpChargeOrLandingPhysics(false);
        }
        @Override
        public void update(Player entity) {
            if (entity.isGroundedNormal()) {
                entity.stateMachine.changeState(PlayerStateMachine.RUN);
            } else {
                if (entity.freeFromLockout()) {
                    entity.stateMachine.changeState(PlayerStateMachine.JUMP_DESCEND);
                }

            }
        }

        @Override
        public void exit (Player entity) {
            entity.attacking = false;
        }
    },
    */
    ATTACK_DIVE() {
        @Override
        public void enter(Player entity) {
            entity.initDiveKickPhysics();
            entity.attacking = true;
        }

        @Override
        public void update(Player entity) {
            if (entity.isGroundedNormal()) {
                //entity.stateMachine.changeState(PlayerStateMachine.ATTACK_JUMP_LANDING);
                entity.stateMachine.changeState(PlayerStateMachine.RUN);
            } else {
                entity.continueDivePhysics();
            }
        }

        @Override
        public void exit (Player entity) {
            entity.attacking = false;
        }
    },
    DAMAGE_KNOCKBACK() {
        @Override
        public void enter(Player entity) {
            entity.initDamagePhysics();
            entity.invincible = true;
        }

        @Override
        public void update(Player entity) {
            if (entity.isGroundedNormal()) {
                /** TODO: Once health is added, change this to damage or death landing */
                if (entity.isDead()) {
                    entity.stateMachine.changeState(PlayerStateMachine.DEATH_LANDING);
                } else {
                    entity.stateMachine.changeState(PlayerStateMachine.DAMAGE_LANDING);
                }
            }
        }
    },
    DAMAGE_LANDING() {
        @Override
        public void update(Player entity) {
            if (entity.freeFromLockout()) {
                if (entity.isGroundedNormal()) {
                    entity.stateMachine.changeState(PlayerStateMachine.RUN);
                } else {
                    entity.stateMachine.changeState(PlayerStateMachine.JUMP_DESCEND);
                }

                // Run away after getting hit
                entity.changeDirection();

//                // Change dir if this is a tutorial enemy
//                if (entity.tutorialHit) {
//                    entity.changeDirection();
//                    entity.tutorialHit = false;
//                }
            } else {
                entity.halfVelocities();
            }
        }
        @Override
        public void exit (Player entity) {
            entity.invincible = false;
        }
    },
    DEATH_LANDING() {
        @Override
        public void enter(Player entity) {
            entity.initDeath();
        }
        @Override
        public void exit (Player entity) {
            entity.invincible = false;
        }
    }
    ;

    @Override
    public void enter(Player entity) {
    }

    @Override
    public void update(Player entity) {
    }

    @Override
    public void exit(Player entity) {

    }

    @Override
    public boolean onMessage(Player entity, Telegram telegram) {
        return false;
    }
}
