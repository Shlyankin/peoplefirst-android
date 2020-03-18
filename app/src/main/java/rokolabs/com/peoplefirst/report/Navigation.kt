package rokolabs.com.peoplefirst.report

import rokolabs.com.peoplefirst.R

class Navigation {
    companion object {

        public fun getPrevFragmentIdVictimCreateReport(currentFragmentId: Int): Int {
            when(currentFragmentId) {
                R.id.nav_harassment_type -> {
                    return R.id.nav_main_questions
                }
                R.id.nav_harassment_reasons -> {
                    return R.id.nav_harassment_type
                }
                R.id.nav_report_happened_before -> {
                    return R.id.nav_harassment_reasons
                }
                R.id.nav_report_what_happened -> {
                    return R.id.nav_report_happened_before
                }
                R.id.nav_report_who_being_harassed -> {
                    return R.id.nav_report_what_happened
                }
                R.id.nav_report_who_agressor_was -> {
                    return R.id.nav_report_who_being_harassed
                }
                R.id.nav_report_were_any_witnesses -> {
                    return R.id.nav_report_who_agressor_was
                }
                R.id.nav_report_place -> {
                    return R.id.nav_report_were_any_witnesses
                }
                R.id.nav_report_date_time -> {
                    return R.id.nav_report_place
                }
                R.id.nav_report_how_resolved -> {
                    return R.id.nav_report_date_time
                }
                R.id.nav_report_witness_information -> {
                    return R.id.nav_report_how_resolved
                }
                R.id.nav_report_summary -> {
                    return R.id.nav_report_how_resolved
                }
                R.id.nav_report_profile_confirmation -> {
                    return R.id.nav_report_summary
                }
                else -> {
                    return R.id.nav_main_questions
                }
            }
        }

        public fun getPrevFragmentIdVerifyVictim(currentFragmentId: Int): Int {
            when(currentFragmentId) {

                R.id.nav_harassment_reasons -> {
                    return R.id.nav_harassment_type
                }
                R.id.nav_report_happened_before -> {
                    return R.id.nav_harassment_reasons
                }
                R.id.nav_report_what_happened -> {
                    return R.id.nav_report_happened_before
                }
                R.id.nav_report_who_agressor_was -> {
                    return R.id.nav_report_what_happened
                }
                R.id.nav_report_were_any_witnesses -> {
                    return R.id.nav_report_who_agressor_was
                }
                R.id.nav_report_how_resolved -> {
                    return R.id.nav_report_were_any_witnesses
                }
                R.id.nav_report_summary -> {
                    return R.id.nav_report_how_resolved
                }
                R.id.nav_report_profile_confirmation -> {
                    return R.id.nav_report_summary
                }
                else -> {
                    return R.id.nav_harassment_type
                }
            }
        }

        public fun getPrevFragmentIdVerifyAggressor(currentFragmentId: Int): Int {
            when(currentFragmentId) {
                R.id.nav_report_were_any_witnesses -> {
                    return R.id.nav_report_what_happened
                }
                R.id.nav_report_summary -> {
                    return R.id.nav_report_were_any_witnesses
                }
                R.id.nav_report_profile_confirmation -> {
                    return R.id.nav_report_summary
                }
                else -> {
                    return R.id.nav_report_what_happened
                }
            }
        }

        public fun getPrevFragmentIdVerifyWitness(currentFragmentId: Int): Int {
            when(currentFragmentId) {
                R.id.nav_harassment_reasons -> {
                    return R.id.nav_harassment_type
                }
                R.id.nav_report_happened_before -> {
                    return R.id.nav_harassment_reasons
                }
                R.id.nav_report_what_happened -> {
                    return R.id.nav_report_happened_before
                }
                R.id.nav_report_who_agressor_was -> {
                    return R.id.nav_report_what_happened
                }
                R.id.nav_report_were_any_witnesses -> {
                    return R.id.nav_report_who_agressor_was
                }
                R.id.nav_report_summary -> {
                    return R.id.nav_report_were_any_witnesses
                }
                R.id.nav_report_profile_confirmation -> {
                    return R.id.nav_report_summary
                }
                else -> {
                    return R.id.nav_harassment_type
                }
            }
        }

        public fun getNextFragmentIdVictimCreateReport(currentFragmentId: Int): Int {
            when(currentFragmentId) {
                R.id.nav_main_questions -> {
                    return R.id.nav_harassment_type
                }
                R.id.nav_harassment_type -> {
                    return R.id.nav_harassment_reasons
                }
                R.id.nav_harassment_reasons -> {
                    return R.id.nav_report_happened_before
                }
                R.id.nav_report_happened_before -> {
                    return R.id.nav_report_what_happened
                }
                R.id.nav_report_what_happened -> {
                    return R.id.nav_report_who_being_harassed
                }
                R.id.nav_report_who_being_harassed -> {
                    return R.id.nav_report_who_agressor_was
                }
                R.id.nav_report_who_agressor_was -> {
                    return R.id.nav_report_were_any_witnesses
                }
                // if were witnesses then select it in witnesses fragment and check them
                R.id.nav_report_were_any_witnesses -> {
                    return R.id.nav_report_place
                }
                R.id.nav_report_place -> {
                    return R.id.nav_report_date_time
                }
                R.id.nav_report_date_time -> {
                    return R.id.nav_report_how_resolved
                }
                R.id.nav_report_how_resolved -> {
                    return R.id.nav_report_summary
                }
                R.id.nav_report_summary -> {
                    return R.id.nav_report_profile_confirmation
                }
                // then "submitted screen" called from nav_report_profile_confirmation or nav_report_summary
                else -> {
                    return R.id.nav_main_questions
                }
            }
        }

        public fun getNextFragmentIdVerifyVictim(currentFragmentId: Int): Int {
            when(currentFragmentId) {
                R.id.nav_harassment_type -> {
                    return R.id.nav_harassment_reasons
                }
                R.id.nav_harassment_reasons -> {
                    return R.id.nav_report_happened_before
                }
                R.id.nav_report_happened_before -> {
                    return R.id.nav_report_what_happened
                }
                R.id.nav_report_what_happened -> {
                    return R.id.nav_report_who_agressor_was
                }
                R.id.nav_report_who_agressor_was -> {
                    return R.id.nav_report_were_any_witnesses
                }
                R.id.nav_report_were_any_witnesses -> {
                    return R.id.nav_report_how_resolved
                }
                R.id.nav_report_how_resolved -> {
                    return R.id.nav_report_summary
                }
                R.id.nav_report_summary -> {
                    return R.id.nav_report_profile_confirmation
                }
                else -> {
                    return R.id.nav_harassment_type
                }
            }
        }

        public fun getNextFragmentIdVerifyAggressor(currentFragmentId: Int): Int {
            when(currentFragmentId) {
                R.id.nav_report_what_happened -> {
                    return R.id.nav_report_were_any_witnesses
                }
                R.id.nav_report_were_any_witnesses -> {
                    return R.id.nav_report_summary
                }
                R.id.nav_report_summary -> {
                    return R.id.nav_report_profile_confirmation
                }
                else -> {
                    return R.id.nav_report_what_happened
                }
            }
        }

        public fun getNextFragmentIdVerifyWitness(currentFragmentId: Int): Int {
            when(currentFragmentId) {
                R.id.nav_harassment_type -> {
                    return R.id.nav_harassment_reasons
                }
                R.id.nav_harassment_reasons -> {
                    return R.id.nav_report_happened_before
                }
                R.id.nav_report_happened_before -> {
                    return R.id.nav_report_what_happened
                }
                R.id.nav_report_what_happened -> {
                    return R.id.nav_report_who_agressor_was
                }
                R.id.nav_report_who_agressor_was -> {
                    return R.id.nav_report_were_any_witnesses
                }
                R.id.nav_report_were_any_witnesses -> {
                    return R.id.nav_report_summary
                }
                R.id.nav_report_summary -> {
                    return R.id.nav_report_profile_confirmation
                }
                else -> {
                    return R.id.nav_harassment_type
                }
            }
        }

    }
}