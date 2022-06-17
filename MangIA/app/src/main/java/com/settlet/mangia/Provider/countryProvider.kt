package com.settlet.mangia.Provider

import com.blongho.country_data.World

class countryProvider {
    companion object{
            val lAsia = paisesAsia()
            val lEurope = paisesEuropa()
            val lOceania = paisesOceania()
            val lNAmerica = paisesANor()
            val lSAmerica = paisesASur()
            val lAfrican = paisesAfrica()
            val lTPaises = todosPaises()

        private fun todosPaises():MutableList<String>
        {
            val listCountries = World.getAllCountries()
            val listCountriesNames: MutableList<String> = mutableListOf()
            for(c in listCountries)
            {
                listCountriesNames.add(c.name).toString()
            }
            return listCountriesNames
        }
        private fun paisesAsia():MutableList<String>
        {
            val listAsian = World.getCountriesFrom(World.Continent.ASIA)
            val listAsianNames: MutableList<String> = mutableListOf()
            for(c in listAsian)
            {
                listAsianNames.add(c.name).toString()
            }
            return listAsianNames
        }
        private fun paisesAfrica():MutableList<String>
        {
            val listAfrican = World.getCountriesFrom(World.Continent.AFRICA)
            val listAfricanNames: MutableList<String> = mutableListOf()
            for(c in listAfrican)
            {
                listAfricanNames.add(c.name).toString()
            }
            return listAfricanNames
        }
        private fun paisesEuropa():MutableList<String>
        {
            val listEurope = World.getCountriesFrom(World.Continent.EUROPE)
            val listEuropeNames: MutableList<String> = mutableListOf()
            for(c in listEurope)
            {
                listEuropeNames.add(c.name).toString()
            }
            return listEuropeNames
        }
        private fun paisesASur():MutableList<String>
        {
            val listASur = World.getCountriesFrom(World.Continent.SOUTH_AMERICA)
            val listASurNames: MutableList<String> = mutableListOf()
            for(c in listASur)
            {
                listASurNames.add(c.name).toString()
            }
            return listASurNames
        }
        private fun paisesANor():MutableList<String>
        {
            val listANor = World.getCountriesFrom(World.Continent.NORTH_AMERICA)
            val listANorNames: MutableList<String> = mutableListOf()
            for(c in listANor)
            {
                listANorNames.add(c.name).toString()
            }
            return listANorNames
        }
        private fun paisesOceania():MutableList<String>
        {
            val listOceania = World.getCountriesFrom(World.Continent.OCEANIA)
            val listOceaniaNames: MutableList<String> = mutableListOf()
            for(c in listOceania)
            {
                listOceaniaNames.add(c.name).toString()
            }
            return listOceaniaNames
        }
    }
}