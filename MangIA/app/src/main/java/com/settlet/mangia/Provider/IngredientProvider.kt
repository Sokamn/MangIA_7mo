package com.settlet.mangia.Provider

import com.google.firebase.storage.FirebaseStorage
import com.settlet.mangia.Model.Ingredient

class IngredientProvider {
    companion object{
        val ingredientListO = listOf<Ingredient>(
            //Especias
            Ingredient(
                "Achiote",
                "Polvos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Achiote.png")
            ),
            Ingredient(
                "Agracejo",
                "Polvos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Agracejo.png")
            ),
            Ingredient(
                "Ajenuz",
                "Polvos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Ajenuz.png")
            ),
            Ingredient(
                "Ajowán",
                "Polvos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Ajowan.png")
            ),
            Ingredient(
                "Albahaca",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Albahaca.png")
            ),
            Ingredient(
                "Alcaravea",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Alcaravea.png")
            ),
            Ingredient(
                "Angélica",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Angelica.png")
            ),
            Ingredient(
                "Anís",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Anis.png")
            ),
            Ingredient(
                "Anís Estrellado",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/AnisEstrellado.png")
            ),
            Ingredient(
                "Apio",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Apio.png")
            ),
            Ingredient(
                "Azafrán",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Azafran.png")
            ),
            Ingredient(
                "Canela",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Canela.png")
            ),
            Ingredient(
                "Cardamomo",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Cardamomo.png")
            ),
            Ingredient(
                "Carom",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Carom.png")
            ),
            Ingredient(
                "Casia",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Casia.png")
            ),
            Ingredient(
                "Cebolla deshidratada",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/CebollaDeshidratada.png")
            ),
            Ingredient(
                "Cebollino",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Cebollino.png")
            ),
            Ingredient(
                "Chile",
                "Polvo",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Chile.png")
            ),
            Ingredient(
                "Chiles mexicanos",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/ChilesMexicanos.png")
            ),
            Ingredient(
                "Cilantro",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Cilantro.png")
            ),
            Ingredient(
                "Clavo",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Clavo.png")
            ),
            Ingredient(
                "Comino",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Comino.png")
            ),
            Ingredient(
                "Cúrcuma",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Curcuma.png")
            ),
            Ingredient(
                "Dashi no moto",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/DashiNoMoto.png")
            ),
            Ingredient(
                "Enebro",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Enebro.png")
            ),
            Ingredient(
                "Eneldo",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Eneldo.png")
            ),
            Ingredient(
                "Epazote",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Epazote.png")
            ),
            Ingredient(
                "Estragón",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Estragon.png")
            ),
            Ingredient(
                "Fenogreco",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Fenogreco.png")
            ),
            Ingredient(
                "Galangal o galanga",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Galangal.png")
            ),
            Ingredient(
                "Granos del Paraíso",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/GranosDelParaiso.png")
            ),
            Ingredient(
                "Guindilla ojo de pájaro",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/GuindillaOjoDePajaro.png")
            ),
            Ingredient(
                "Haba tonka",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/HabTonka.png")
            ),
            Ingredient(
                "Hierba limón",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/HierbaLimon.png")
            ),
            Ingredient(
                "Hierbabuena o Sándalo",
                "Unidad",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Hierbabuena.png")
            ),
            Ingredient(
                "Hinojo",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Hinojo.png")
            ),
            Ingredient(
                "Hoja de curry",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/HojaDeCurry.png")
            ),
            Ingredient(
                "Laurel",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Laurel.png")
            ),
            Ingredient(
                "Lirio deshidratado",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/LirioDeshidratado.png")
            ),
            Ingredient(
                "Maca",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Maca.png")
            ),
            Ingredient(
                "Macis",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Macis.png")
            ),
            Ingredient(
                "Mango verde deshidratado",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/MangoVerdeDeshidratado.png")
            ),
            Ingredient(
                "Mejorana",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Mejorana.png")
            ),
            Ingredient(
                "Menta",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Menta.png")
            ),
            Ingredient(
                "Mezcla de 5 especias china",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/MezclaDe5EspeciasChina.png")
            ),
            Ingredient(
                "Provenzal",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Provenzal.png")
            ),
            Ingredient(
                "Mostaza",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Mostaza.png")
            ),
            Ingredient(
                "Nigela sativa",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/NigelaSativa.png")
            ),

            Ingredient(
                "Nueces areca",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/NuecesAreca.png")
            ),
            Ingredient(
                "Nuez moscada",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/NuezMoscada.png")
            ),
            Ingredient(
                "Orégano",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Oregano.png")
            ),
            Ingredient(
                "Perejil",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Perejil.png")
            ),
            Ingredient(
                "Pimentón",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Pimenton.png")
            ),
            Ingredient(
                "Pimienta",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Pimienta.png")
            ),
            Ingredient(
                "Pimienta blanca",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/PimientaBlanca.png")
            ),
            Ingredient(
                "Pimienta de cayena",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/PimientaDeCayena.png")
            ),
            Ingredient(
                "Pimienta de Guinea",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/PimientaDeGuinea.png")
            ),
            Ingredient(
                "Pimienta de Jamaica",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/PimientaDeJamaica.png")
            ),
            Ingredient(
                "Pimienta de Sichuan",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/PimientaDeSichuan.png")
            ),
            Ingredient(
                "Pimienta larga",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/PimientaLarga.png")
            ),
            Ingredient(
                "Pimienta negra",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/PimientaNegra.png")
            ),
            Ingredient(
                "Pimienta timiz",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/PimientaTimiz.png")
            ),
            Ingredient(
                "Romero",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Romero.png")
            ),
            Ingredient(
                "Sal",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Sal.png")
            ),
            Ingredient(
                "Sal de ajo",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/SalDeAjo.png")
            ),
            Ingredient(
                "Sal de apio",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/SalDeApio.png")
            ),
            Ingredient(
                "Salvia",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Salvia.png")
            ),
            Ingredient(
                "Satureja",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Satureja.png")
            ),
            Ingredient(
                "Semilla de amapola",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/SemillaDeAmapola.png")
            ),
            Ingredient(
                "Semilla de apio",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/SemillaDeApio.png")
            ),
            Ingredient(
                "Semillas de Chía",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/SemillasDeChía.png")
            ),
            Ingredient(
                "Sésamo",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Sesamo.png")
            ),
            Ingredient(
                "Spirulina",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Spirulina.png")
            ),
            Ingredient(
                "Tamarindo",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Tamarindo.png")
            ),
            Ingredient(
                "Tomillo",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Tomillo.png")
            ),
            Ingredient(
                "Vainilla",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Vainilla.png")
            ),
            Ingredient(
                "Yuzu",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Yuzu.png")
            ),
            Ingredient(
                "Zumaque",
                "Gramos",
                0F,
                0,
                FirebaseStorage.getInstance().reference.child("ingredients/Zumaque.png")
            ),
        //Ingredientes de cocina
             Ingredient(
                 "Zaatar",
            "Unidad",
            0F,
            0,
                 FirebaseStorage.getInstance().reference.child("ingredients/Zaatar.png")
        ),

        Ingredient(
        "Yuca",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Yuca.png")
        ),

        Ingredient(
        "Yaca",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Yaca.png")
        ),

        Ingredient(
        "Wasabi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Wasabi.png")
        ),

        Ingredient(
        "Wakame",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Wakame.png")
        ),

        Ingredient(
        "Virutas y serrín para ahumar",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VirutasYSerrinParaAhumar.png")
        ),

        Ingredient(
        "Virutas de madera para aromatizar cerveza",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VirutasDeMaderaParaAromatizarCerveza.png")
        ),

        Ingredient(
        "Vino Mirín",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VinoMirin.png")
        ),

        Ingredient(
        "Vino de Marsala",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VinoDeMarsala.png")
        ),

        Ingredient(
        "VinoDeArrozShaoxing",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VinoDeArrozShaoxing.png")
        ),

        Ingredient(
        "Vino de arroz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VinoDeArroz.png")
        ),

        Ingredient(
        "Vinagre preparado para sushi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VinagrePreparadoParaSushi.png")
        ),

        Ingredient(
        "Vinagre negro",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VinagreNegro.png")
        ),

        Ingredient(
        "Vinagre destilado",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VinagreDestilado.png")
        ),

        Ingredient(
        "Vinagre de arroz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/VinagreDeArroz.png")
        ),

        Ingredient(
        "Vinagre",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Vinagre.png")
        ),

        Ingredient(
        "Udon",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Udon.png")
        ),

        Ingredient(
        "Tsukemono",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Tsukemono.png")
        ),

        Ingredient(
        "Trufa",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Trufa.png")
        ),

        Ingredient(
        "Tripa natural",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/TripaNatural.png")
        ),

        Ingredient(
        "Trigo mote",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/TrigoMote.png")
        ),

        Ingredient(
        "Tofu",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Tofu.png")
        ),

        Ingredient(
        "Tirabeques",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Tirabeques.png")
        ),

        Ingredient(
        "Teff",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Teff.png")
        ),

        Ingredient(
        "Té Oolong",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/TeOolong.png")
        ),

        Ingredient(
        "Té Gunpowder",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/TeGunpowder.png")
        ),

        Ingredient(
        "Tapioca",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Tapioca.png")
        ),

        Ingredient(
        "Tallarines de arroz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/TallarinesDeArroz.png")
        ),

        Ingredient(
        "Takuan o danmuji",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/TakuanODanmuji.png")
        ),

        Ingredient(
        "Tahina",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Tahina.png")
        ),

        Ingredient(
        "Somen",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Somen.png")
        ),

        Ingredient(
        "Soja texturizada",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SojaTexturizada.png")
        ),

        Ingredient(
        "Soja",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Soja.png")
        ),

        Ingredient(
        "Soba",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Soba.png")
        ),

        Ingredient(
        "Skyr",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Skyr.png")
        ),

        Ingredient(
        "Sirope de jengibre",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SiropeDeJengibre.png")
        ),

        Ingredient(
        "Shiso",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Shiso.png")
        ),

        Ingredient(
        "Shichimi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Shichimi.png")
        ),

        Ingredient(
        "Setas shiitake",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SetasShiitake.png")
        ),

        Ingredient(
        "Setas deshidratadas de España",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SetasDeshidratadasDeEspaña.png")
        ),

        Ingredient(
        "Serpol",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Serpol.png")
        ),

        Ingredient(
        "Sémola de trigo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SemolaDeTrigo.png")
        ),

        Ingredient(
        "Semillas de loto",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SemillasDeLoto.png")
        ),

        Ingredient(
        "Seitán",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Seitan.png")
        ),

        Ingredient(
        "Sansho",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Sansho.png")
        ),

        Ingredient(
        "Sambal",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Sambal.png")
        ),

        Ingredient(
        "Salsifí",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Salsifi.png")
        ),

        Ingredient(
        "Salsas famosas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsasFamosas.png")
        ),

        Ingredient(
        "Salsas de chiles",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsasDeChiles.png")
        ),

        Ingredient(
        "Salsa yakitori",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaYakitori.png")
        ),

        Ingredient(
        "Salsa Worcestershire",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaWorcestershire.png")
        ),

        Ingredient(
        "Salsa verde mexicana",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaVerdeMexicana.png")
        ),

        Ingredient(
        "Salsa Unagi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaUnagi.png")
        ),

        Ingredient(
        "Salsa tonkatsu",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaTonkatsu.png")
        ),

        Ingredient(
        "Salsa Teriyaki",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaTeriyaki.png")
        ),

        Ingredient(
        "Salsa tamari",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaTamari.png")
        ),

        Ingredient(
        "Salsa Sriracha",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaSriracha.png")
        ),
        Ingredient(
        "Salsa Ponzu",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaPonzu.png")
        ),
        Ingredient(
        "Salsa para tempura",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaParaTempura.png")
        ),
        Ingredient(
        "Salsa para sukiyaki",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaParaSukiyaki.png")
        ),
        Ingredient(
        "Salsa mexicana",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Salsa mexicana.png")
        ),
        Ingredient(
        "Salsa Hoisin",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaHoisin.png")
        ),
        Ingredient(
        "Salsa Harissa",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaHarissa.png")
        ),
        Ingredient(
        "Salsa Galbi o Kalbi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaGalbi_o_Kalbi.png")
        ),
        Ingredient(
        "Salsa de Soja Oscura",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaDeSojaOscura.png")
        ),
        Ingredient(
        "Salsa de soja ligera",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaDeSojaLigera.png")
        ),
        Ingredient(
        "Salsa de soja",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaDeSoja.png")
        ),
        Ingredient(
        "Salsa de sésamo para shabu",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaDeSesamoParaShabu.png")
        ),
        Ingredient(
        "Salsa de pescado",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaDePescado.png")
        ),
        Ingredient(
        "Salsa de ostras",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaDeOstras.png")
        ),
        Ingredient(
        "Salsa de mostaza",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaDeMostaza.png")
        ),
        Ingredient(
        "Salsa de menta",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaDeMenta.png")
        ),
        Ingredient(
        "Salsa de alubias picante",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaDeAlubiasPicante.png")
        ),
        Ingredient(
        "Salsa Chakalaka",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaChakalaka.png")
        ),
        Ingredient(
        "Salsa Barbacoa",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaBarbacoa.png")
        ),
        Ingredient(
        "Salsa agridulce china",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalsaAgridulceChina.png")
        ),
        Ingredient(
        "Sal rosada del Himalaya",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalRosadaDelHimalaya.png")
        ),
        Ingredient(
        "Sal negra",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalNegra.png")
        ),
        Ingredient(
        "Sal Kosher",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/SalKosher.png")
        ),
        Ingredient(
        "Sake",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Sake.png")
        ),
        Ingredient(
        "Dumplings (rellenos) asiáticos",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Dumplings(rellenos)Asiaticos.png")
        ),
        Ingredient(
        "Rebozado para tempura",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/RebozadoParaTempura.png")
        ),
        Ingredient(
        "Ramen",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Ramen.png")
        ),
        Ingredient(
        "Rambután",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Rambutan.png")
        ),
        Ingredient(
        "Rábano picante",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/RabanoPicante.png")
        ),
        Ingredient(
        "Quínoa",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Quinoa.png")
        ),
        Ingredient(
        "Ptitim",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Ptitim.png")
        ),
        Ingredient(
        "Preparado de curry verde",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PreparadoDeCurryVerde.png")
        ),
        Ingredient(
        "Preparado de curry rojo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PreparadoDeCurryRojo.png")
        ),
        Ingredient(
        "Preparado de curry amarillo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PreparadoDeCurryAmarillo.png")
        ),
        Ingredient(
        "Huevos",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Huevos.png")
        ),
        Ingredient(
        "Polenta",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Polenta.png")
        ),
        Ingredient(
        "Plátano macho",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PlatanoMacho.png")
        ),
        Ingredient(
        "Pistacho",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Pistacho.png")
        ),
        Ingredient(
        "Pisco",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Pisco.png")
        ),
        Ingredient(
        "Pimientos choriceros",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PimientosChoriceros.png")
        ),
        Ingredient(
        "Penicillium Roqueforti",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PenicilliumRoqueforti.png")
        ),
        Ingredient(
        "Penicillium Camemberti",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PenicilliumCamemberti.png")
        ),
        Ingredient(
        "Pastas asiáticas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PastasAsiaticas.png")
        ),
        Ingredient(
        "Pasta de gambas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PastaDeGambas.png")
        ),
        Ingredient(
        "Pasta de chiles",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PastaDeChiles.png")
        ),
        Ingredient(
        "Pasta de avellanas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PastaDeAvellanas.png")
        ),
        Ingredient(
        "Pasta de arroz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PastaDeArroz.png")
        ),
        Ingredient(
        "Pasta de alubias fermentada",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PastaDeAlubiasFermentada.png")
        ),
        Ingredient(
        "Pasta de almendra",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PastaDeAlmendra.png")
        ),
        Ingredient(
        "Papel de arroz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PapelDeArroz.png")
        ),
        Ingredient(
        "Papadom",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Papadom.png")
        ),
        Ingredient(
        "Papa seca",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PapaSeca.png")
        ),
        Ingredient(
        "Panko",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Panko.png")
        ),
        Ingredient(
        "Panela",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Panela.png")
        ),
        Ingredient(
        "Pan de gambas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PanDeGambas.png")
        ),
        Ingredient(
        "Palomitas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Palomitas.png")
        ),
        Ingredient(
        "Paloduz o palo de regaliz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Paloduz_o_PaloDeRegaliz.png")
        ),
        Ingredient(
        "Palmito",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Palmito.png")
        ),
        Ingredient(
        "Pak Choi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/PakChoi.png")
        ),
        Ingredient(
        "Oreja De Madera",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/OrejaDeMadera.png")
        ),
        Ingredient(
        "Okra",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Okra.png")
        ),
        Ingredient(
        "Ojo de dragón",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/OjoDeDragon.png")
        ),
        Ingredient(
        "Ogbono",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Ogbono.png")
        ),
        Ingredient(
        "Nori",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Nori.png")
        ),
        Ingredient(
        "Ñoras",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Ñoras.png")
        ),
        Ingredient(
        "Nopales",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Nopales.png")
        ),
        Ingredient(
        "Nigari",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Nigari.png")
        ),
        Ingredient(
        "Niboshi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Niboshi.png")
        ),
        Ingredient(
        "Ñame",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Ñame.png")
        ),
        Ingredient(
        "Mole",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Mole.png")
        ),
        Ingredient(
        "Mojos canarios",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MojosCanarios.png")
        ),
        Ingredient(
        "Miso",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Miso.png")
        ),
        Ingredient(
        "Mijo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Mijo.png")
        ),
        Ingredient(
        "Mezcla tandoori",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MezclaTandoori.png")
        ),
        Ingredient(
        "Mezcla ras el hanout",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MezclaRasElHanout.png")
        ),
        Ingredient(
        "Mezcla para satay",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MezclaParaSatay.png")
        ),
        Ingredient(
        "Mezcla para Kimchi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MezclaParaKimchi.png")
        ),
        Ingredient(
        "Mezcla jambalaya",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MezclaLambalaya.png")
        ),
        Ingredient(
        "Mezcla de curry Panang",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MezclaDeCurryPanang.png")
        ),
        Ingredient(
        "Mezcla de curry",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MezclaDeCurry.png")
        ),
        Ingredient(
        "Mezcla cajún",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MezclaCajun.png")
        ),
        Ingredient(
        "Melaza de granada",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MelazaDeGranada.png")
        ),
        Ingredient(
        "Mei fun",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MeiFun.png")
        ),
        Ingredient(
        "Mazapán",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Mazapan.png")
        ),
        Ingredient(
        "Mantequilla de cacahuetes",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MantequillaDeCacahuetes.png")
        ),
        Ingredient(
        "Mantequilla clarificada",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MantequillaClarificada.png")
        ),
        Ingredient(
        "Mango Alphonso",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MangoAlphonso.png")
        ),
        Ingredient(
        "Malva",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Malva.png")
        ),
        Ingredient(
        "Maíz mote",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MaizMote.png")
        ),
        Ingredient(
        "Maíz morado",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MaizMorado.png")
        ),
        Ingredient(
        "Maíz en copos",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MaizEnCopos.png")
        ),
        Ingredient(
        "Maíz chulpi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MaizChulpi.png")
        ),
        Ingredient(
        "Maíz cancha",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MaizCancha.png")
        ),
        Ingredient(
        "Maíz baby",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MaizBaby.png")
        ),
        Ingredient(
        "Maderas para ahumar de Smokey Olive Wood",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/MaderasParaAhumarDeSmokeyOliveWood.png")
        ),
        Ingredient(
        "Lino",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Lino.png")
        ),
        Ingredient(
        "Limas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Limas.png")
        ),
        Ingredient(
        "Limones",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Limones.png")
        ),
        Ingredient(
        "Lima Kaffir",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/LimaKaffir.png")
        ),
        Ingredient(
        "Lichi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Lichi.png")
        ),
        Ingredient(
        "Levadura para hacer cerveza",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/LevaduraParaHacerCerveza.png")
        ),
        Ingredient(
        "Levadura",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Levadura.png")
        ),
        Ingredient(
        "Lentejas rojas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/LentejasRojas.png")
        ),
        Ingredient(
        "Lentejas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Lentejas.png")
        ),
        Ingredient(
        "Lenteja Du Puy",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/LentejaDuPuy.png")
        ),
        Ingredient(
        "Leche de coco",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/LecheDeCoco.png")
        ),
        Ingredient(
        "Kombu",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Kombu.png")
        ),
        Ingredient(
        "Kizami Nori",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/KizamiNori.png")
        ),
        Ingredient(
        "Katsuobushi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Katsuobushi.png")
        ),
        Ingredient(
        "Judias Mungo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/JudiasMungo.png")
        ),
        Ingredient(
        "Jengibre encurtido blanco o rosa",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/JengibreEncurtidoBlanco_O_Rosa.png")
        ),
        Ingredient(
        "Jengibre encurtido",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Jengibre encurtido.png")
        ),
        Ingredient(
        "Jengibre cristalizado",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/JengibreCristalizado.png")
        ),
        Ingredient(
        "Jengibre",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Jengibre.png")
        ),
        Ingredient(
        "Jarabe de maíz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/JarabeDeMaiz.png")
        ),
        Ingredient(
        "Jarabe de agave",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/JarabeDeAgave.png")
        ),
        Ingredient(
        "Ikan Bilis",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/IkanBilis.png")
        ),
        Ingredient(
        "Huevo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Huevo.png")
        ),
        Ingredient(
        "Hojas de mazorca de maíz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HojasDeMazorcaDeMaiz.png")
        ),
        Ingredient(
        "Hojas de limonar",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HojasDeLimonar.png")
        ),
        Ingredient(
        "Hojas de kaffir",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HojasDeKaffir.png")
        ),
        Ingredient(
        "Hoja de parra",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HojaDeParra.png")
        ),
        Ingredient(
        "Ho fun",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HoFun.png")
        ),
        Ingredient(
        "Hierbas chinas para sopa",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HierbasChinasParaSopa.png")
        ),
        Ingredient(
        "Harina para chapati",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaParaChapati.png")
        ),
        Ingredient(
        "Harina de yuca",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaDeYuca.png")
        ),
        Ingredient(
        "Harina de soja",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaDeSoja.png")
        ),
        Ingredient(
        "Harina de maíz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaDeMaiz.png")
        ),
        Ingredient(
        "Harina de garbanzo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaDeGarbanzo.png")
        ),
        Ingredient(
        "Harina de fuerza",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaDeFuerza.png")
        ),
        Ingredient(
        "Harina de coco",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaDeCoco.png")
        ),
        Ingredient(
        "Harina de arroz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaDeArroz.png")
        ),
        Ingredient(
        "Harina de altramuz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaDeAltramuz.png")
        ),
        Ingredient(
        "Harina de almortas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/HarinaDeAlmortas.png")
        ),
        Ingredient(
        "Guisante o arveja",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Guisante_o_Arveja.png")
        ),
        Ingredient(
        "Guandú",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Guandu.png")
        ),
        Ingredient(
        "Gomasio",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Gomasio.png")
        ),
        Ingredient(
        "Gofio",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Gofio.png")
        ),
        Ingredient(
        "Gochujang",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Gochujang.png")
        ),
        Ingredient(
        "Gochu",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Gochu.png")
        ),
        Ingredient(
        "Gluten",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Gluten.png")
        ),
        Ingredient(
        "Glutamato monosódico",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/GlutamatoMonosodico.png")
        ),
        Ingredient(
        "Germen de trigo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/GermenDeTrigo.png")
        ),
        Ingredient(
        "Geotrichum candidum",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/GeotrichumCandidum.png")
        ),
        Ingredient(
        "Garam Masala",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/GaramMasala.png")
        ),
        Ingredient(
        "Furikake",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Furikake.png")
        ),
        Ingredient(
        "Flor de sal",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FlorDeSal.png")
        ),
        Ingredient(
        "Flor de hibiscus",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FlorDeHibiscus.png")
        ),
        Ingredient(
        "Flor de azahar",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FlorDeAzahar.png")
        ),
        Ingredient(
        "Fideos de cristal",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FideosDeCristal.png")
        ),
        Ingredient(
        "Fermentos Termófilos",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FermentosTermofilos.png")
        ),
        Ingredient(
        "Fermentos para yogur con bifidus",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FermentosParaYogurConBifidus.png")
        ),
        Ingredient(
        "Fermentos para nata agria",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FermentosParaNataAgria.png")
        ),
        Ingredient(
        "Fermentos Mesófilos",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FermentosMesofilos.png")
        ),
        Ingredient(
        "Fermento de yogur de Bulgaria",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FermentoDeYogurDeBulgaria.png")
        ),
        Ingredient(
        "Fécula",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Fecula.png")
        ),
        Ingredient(
        "Farro",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Farro.png")
        ),
        Ingredient(
        "Extracto de levadura",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ExtractoDeLevadura.png")
        ),
        Ingredient(
        "Estevia o Stevia",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Estevia_O_Stevia.png")
        ),
        Ingredient(
        "Espelta",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Espelta.png")
        ),
        Ingredient(
        "El té",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ElTe.png")
        ),
        Ingredient(
        "Egusi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Egusi.png")
        ),
        Ingredient(
        "Duqqa",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Duqqa.png")
        ),
        Ingredient(
        "Dulce de leche",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/DulceDeLeche.png")
        ),
        Ingredient(
        "Fécula",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Fecula.png")
        ),
        Ingredient(
        "Almidón",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Almidon.png")
        ),
        Ingredient(
        "Couscous",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Couscous.png")
        ),
        Ingredient(
        "Bulgur",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Bulgur.png")
        ),
        Ingredient(
        "Dashi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Dashi.png")
        ),
        Ingredient(
        "Daikon",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Daikon.png")
        ),
        Ingredient(
        "Cuscús",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Cuscus.png")
        ),
        Ingredient(
        "Curry Massaman",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/CurryMassaman.png")
        ),
        Ingredient(
        "Cultivos y mohos lácticos",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/CultivosYMohosLacticos.png")
        ),
        Ingredient(
        "Cultivos de kéfir",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/CultivosDeKefir.png")
        ),
        Ingredient(
        "Cuitlacoche",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Cuitlacoche.png")
        ),
        Ingredient(
        "Cuajo de ternera",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/CuajoDeTernera.png")
        ),
        Ingredient(
        "Cuajo de cardo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/CuajoDeCardo.png")
        ),
        Ingredient(
        "Cola de pescado",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ColaDePescado.png")
        ),
        Ingredient(
        "Col rizada o Kale",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ColRizada_O_Kale.png")
        ),
        Ingredient(
        "Coco",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Coco.png")
        ),
        Ingredient(
        "Chutney",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Chutney.png")
        ),
        Ingredient(
        "Chuño",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Chuño.png")
        ),
        Ingredient(
        "Chucrut",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Chucrut.png")
        ),
        Ingredient(
        "Chiles chipotle",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ChilesChipotle.png")
        ),
        Ingredient(
        "Chile pasilla",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ChilePasilla.png")
        ),
        Ingredient(
        "Chile guajillo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ChileGuajillo.png")
        ),
        Ingredient(
        "Chile en polvo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ChileEnPolvo.png")
        ),
        Ingredient(
        "Chile de árbol",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ChileDeArbol.png")
        ),
        Ingredient(
        "Chile ancho",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ChileAncho.png")
        ),
        Ingredient(
        "Chatni",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Chatni.png")
        ),

        Ingredient(
        "Achaar",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Achaar.png")
        ),
        Ingredient(
        "Raita",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Raita.png")
        ),
        Ingredient(
        "Chalotas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Chalotas.png")
        ),
        Ingredient(
        "Cebolla de primavera",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/CebollaDePrimavera.png")
        ),
        Ingredient(
        "Castañas de agua",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/CastañasDeAgua.png")
        ),
        Ingredient(
        "Buttermilk",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Buttermilk.png")
        ),
        Ingredient(
        "Bulgur",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Bulgur.png")
        ),
        Ingredient(
        "Brotes de soja",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/BrotesDeSoja.png")
        ),
        Ingredient(
        "Brotes de bambú",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/BrotesDeBambu.png")
        ),
        Ingredient(
        "Botones de Rosa",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/BotonesDeRosa.png")
        ),
        Ingredient(
        "Boniato",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Boniato.png")
        ),
        Ingredient(
        "Fideos de cristal",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FideosDeCristal.png")
        ),
        Ingredient(
        "Bergamota",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Bergamota.png")
        ),
        Ingredient(
        "Bebida energética Carabao",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/BebidaEnergeticaCarabao.png")
        ),
        Ingredient(
        "Baharat",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Baharat.png")
        ),
        Ingredient(
        "Azuki",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Azuki.png")
        ),
        Ingredient(
        "Fideos de cristal",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/FideosDeCristal.png")
        ),
        Ingredient(
        "Azúcar muscovado",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AzucarMuscovado.png")
        ),
        Ingredient(
        "Azúcar moreno",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AzucarMoreno.png")
        ),
        Ingredient(
        "Azúcar integral de caña",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AzucarIntegralDeCaña.png")
        ),
        Ingredient(
        "Azúcar de palma",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AzucarDePalma.png")
        ),
        Ingredient(
        "Azúcar de Coco",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AzucarDeCoco.png")
        ),
        Ingredient(
        "Azúcar Candi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AzucarCandi.png")
        ),
        Ingredient(
        "Asafétida",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Asafetida.png")
        ),
        Ingredient(
        "Arroz para sushi",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ArrozParaSushi.png")
        ),

        Ingredient(
        "Arroz negro",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ArrozNegro.png")
        ),
        Ingredient(
        "Arroz jasmine",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ArrozJasmine.png")
        ),
        Ingredient(
        "Arroz glutinoso",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ArrozGlutinoso.png")
        ),
        Ingredient(
        "Arroz Basmati",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ArrozBasmati.png")
        ),
        Ingredient(
        "Arroz meloso",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/ArrozMeloso.png")
        ),
        Ingredient(
        "Arame",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Arame.png")
        ),
        Ingredient(
        "Anacardos",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Anacardos.png")
        ),
        Ingredient(
        "Amaranto",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Amaranto.png")
        ),
        Ingredient(
        "Almidón de Tapioca",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AlmidonDeTapioca.png")
        ),
        Ingredient(
        "Almidón",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Almidon.png")
        ),
        Ingredient(
        "Alga hijiki",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AlgaHijiki.png")
        ),
        Ingredient(
        "Ajvar",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Ajvar.png")
        ),
        Ingredient(
        "Ajo negro",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AjoNegro.png")
        ),
        Ingredient(
        "Ajo en polvo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AjoEnPolvo.png")
        ),
        Ingredient(
        "Ají Panca",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AjiPanca.png")
        ),
        Ingredient(
        "Ají amarillo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AjiAmarillo.png")
        ),
        Ingredient(
        "Aguacate",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Aguacate.png")
        ),
        Ingredient(
        "Agua de rosas",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AguaDeRosas.png")
        ),
        Ingredient(
        "Agua de kewra",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AguaDeKewra.png")
        ),
        Ingredient(
        "Agua de coco",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AguaDeCoco.png")
        ),
        Ingredient(
        "Agua de Azahar",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AguaDeAzahar.png")
        ),
        Ingredient(
        "Acerola",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Acerola.png")
        ),
        Ingredient(
        "Acelga",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/Acelga.png")
        ),
        Ingredient(
        "Aceite de Soja",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeSoja.png")
        ),
        Ingredient(
        "Aceite de sésamo",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeSesamo.png")
        ),
        Ingredient(
        "Aceite de Oliva",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeOliva.png")
        ),
        Ingredient(
        "Aceite de Girasol",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeGirasol.png")
        ),
        Ingredient(
        "Aceite de coco",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeCoco.png")
        ),
        Ingredient(
        "Aceite de chiles",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeChiles.png")
        ),
        Ingredient(
        "Aceite de Cacahuete",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeCacahuete.png")
        ),
        Ingredient(
        "Aceite de arroz",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeArroz.png")
        ),
        Ingredient(
        "Aceite de argán",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeArgan.png")
        ),
        Ingredient(
        "Aceite de almendras",
        "Unidad",
        0F,
        0,
        FirebaseStorage.getInstance().reference.child("ingredients/AceiteDeAlmendras.png")
        )

        )
        val ingredientListN = IngredientListNames()
        private fun IngredientListNames(): List<String> {
            val listIngredientNames: MutableList<String> = mutableListOf()
            for(i in ingredientListO)
            {
                listIngredientNames.add(i.nombre).toString()
            }
            return listIngredientNames
        }

    }
}