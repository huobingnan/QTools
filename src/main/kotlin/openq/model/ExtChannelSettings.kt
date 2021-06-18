package openq.model

class ExtChannelSettings {
    companion object {
        private val settingDefaultValue  = HashMap<String, String>()
        const val BOND_LENGTH_MAX = "bond length max"
        const val BOND_NUMBER_CONSTRAIN = "bond number constrain"


        init {
            settingDefaultValue[BOND_LENGTH_MAX] = "2.0"
            settingDefaultValue[BOND_NUMBER_CONSTRAIN] = ""
        }

        fun getDefaultValue(key: String): String {
            return settingDefaultValue[key] ?: ""
        }
    }
}