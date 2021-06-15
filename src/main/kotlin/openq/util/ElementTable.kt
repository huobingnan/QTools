package openq.util

/**
 * 元素周期表
 */
class ElementTable {

    companion object {
        private val elementNumberIndexedTable = HashMap<Int, String>()

        init{
            elementNumberIndexedTable[1] = "H"
            elementNumberIndexedTable[2] = "He"
            elementNumberIndexedTable[3] = "Li"
            elementNumberIndexedTable[4] = "Be"
            elementNumberIndexedTable[5] = "B"
            elementNumberIndexedTable[6] = "C"
            elementNumberIndexedTable[7] = "N"
            elementNumberIndexedTable[8] = "O"
            elementNumberIndexedTable[9] = "F"
            elementNumberIndexedTable[10] = "Ne"
            elementNumberIndexedTable[11] = "Na"
            elementNumberIndexedTable[12] = "Mg"
            elementNumberIndexedTable[13] = "Al"
            elementNumberIndexedTable[14] = "Si"
            elementNumberIndexedTable[15] = "P"
            elementNumberIndexedTable[16] = "S"
            elementNumberIndexedTable[17] = "Cl"
            elementNumberIndexedTable[18] = "Ar"
            elementNumberIndexedTable[19] = "K"
            elementNumberIndexedTable[20] = "Ca"
            elementNumberIndexedTable[21] = "Sc"
            elementNumberIndexedTable[22] = "Ti"
            elementNumberIndexedTable[23] = "V"
            elementNumberIndexedTable[24] = "Cr"
            elementNumberIndexedTable[25] = "Mn"
            elementNumberIndexedTable[26] = "Fe"
            elementNumberIndexedTable[27] = "Co"
            elementNumberIndexedTable[28] = "Ni"
            elementNumberIndexedTable[29] = "Cu"
            elementNumberIndexedTable[30] = "Zn"
            elementNumberIndexedTable[31] = "Ga"
            elementNumberIndexedTable[32] = "Ge"
            elementNumberIndexedTable[32] = "Ge"
            elementNumberIndexedTable[33] = "As"
            elementNumberIndexedTable[34] = "Se"
            elementNumberIndexedTable[35] = "Br"
            elementNumberIndexedTable[36] = "Kr"
            elementNumberIndexedTable[37] = "Rb"
            elementNumberIndexedTable[38] = "Sr"
            elementNumberIndexedTable[39] = "Y"
            elementNumberIndexedTable[40] = "Zr"
            elementNumberIndexedTable[41] = "Nb"
            elementNumberIndexedTable[42] = "Mo"
            elementNumberIndexedTable[43] = "Tc"
            elementNumberIndexedTable[44] = "Ru"
            elementNumberIndexedTable[45] = "Rh"
            elementNumberIndexedTable[46] = "Pd"
            elementNumberIndexedTable[47] = "Ag"
            elementNumberIndexedTable[48] = "Cd"
            elementNumberIndexedTable[49] = "In"
            elementNumberIndexedTable[50] = "Sn"
            elementNumberIndexedTable[51] = "Sb"
            elementNumberIndexedTable[52] = "Te"
            elementNumberIndexedTable[53] = "I"
            elementNumberIndexedTable[54] = "Xe"
            elementNumberIndexedTable[41] = "Nb"
            elementNumberIndexedTable[41] = "Nb"
            // 忽略了一些不常见的元素，录入第六周期常用元素
            elementNumberIndexedTable[55] = "Cs"
            elementNumberIndexedTable[56] = "Ba"
            elementNumberIndexedTable[74] = "W"
            elementNumberIndexedTable[75] = "Re"
            elementNumberIndexedTable[76] = "Os"
            elementNumberIndexedTable[77] = "Pt"
            elementNumberIndexedTable[79] = "Au"
            elementNumberIndexedTable[80] = "Hg"
            elementNumberIndexedTable[82] = "Pb"
            elementNumberIndexedTable[83] = "Bi"
        }


        fun getByElementNumber(number: Int): String {
            return elementNumberIndexedTable[number]?:"not record"
        }
    }
}