package controller;

import ordination.*;
import storage.Storage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class Controller {
	private static Storage storage;

	public static void setStorage(Storage storageIn) {
		storage = storageIn;
	}

	/**
	 * Hvis startDato er efter slutDato kastes en IllegalArgumentException og
	 * ordinationen oprettes ikke
	 * Pre: startDen, slutDen, patient og laegemiddel er ikke null
	 * Pre: antal >= 0
	 * @return opretter og returnerer en PN ordination.
	 */
	public static PN opretPNOrdination(LocalDate startDen, LocalDate slutDen,
			Patient patient, Laegemiddel laegemiddel, double antal) {
		if (!checkStartFoerSlut(startDen, slutDen)) {
			throw new IllegalArgumentException("Slutdato kan ikke være før startdato");
		}

		if (patient == null || laegemiddel == null) {
			throw new IllegalArgumentException("Patient og lægemiddel må ikke være null");
		}

		if (antal < 0) {
			throw new IllegalArgumentException("Antal må ikke være negativ");
		}

		PN pn = new PN(startDen, slutDen, laegemiddel, antal);

		pn.setPatient(patient);
		patient.addOrdination(pn);
		return pn;
	}

	/**
	 * Opretter og returnerer en DagligFast ordination. Hvis startDato er efter
	 * slutDato kastes en IllegalArgumentException og ordinationen oprettes ikke
	 * Pre: startDen, slutDen, patient og laegemiddel er ikke null
	 * Pre: margenAntal, middagAntal, aftanAntal, natAntal >= 0
	 */
	public static DagligFast opretDagligFastOrdination(LocalDate startDen,
			LocalDate slutDen, Patient patient, Laegemiddel laegemiddel,
			double morgenAntal, double middagAntal, double aftenAntal,
			double natAntal) {
		if (!checkStartFoerSlut(startDen, slutDen)) {
			throw new IllegalArgumentException("Slutdato kan ikke være før startdato");
		}

		if (patient == null || laegemiddel == null) {
			throw new IllegalArgumentException("Patient og lægemiddel må ikke være null");
		}

		if (morgenAntal < 0 || middagAntal < 0 || aftenAntal < 0 || natAntal < 0) {
			throw new IllegalArgumentException("Antal må ikke være negativ");
		}

		DagligFast dagligFast = new DagligFast(startDen, slutDen, laegemiddel);
		dagligFast.setDosis(0, new Dosis(LocalTime.of(8, 0), morgenAntal));
		dagligFast.setDosis(1, new Dosis(LocalTime.of(12, 0), middagAntal));
		dagligFast.setDosis(2, new Dosis(LocalTime.of(18, 0), aftenAntal));
		dagligFast.setDosis(3, new Dosis(LocalTime.of(23, 0), natAntal));

		dagligFast.setPatient(patient);
		patient.addOrdination(dagligFast);
		return dagligFast;
	}

	/**
	 * Opretter og returnerer en DagligSkæv ordination. Hvis startDato er efter
	 * slutDato kastes en IllegalArgumentException og ordinationen oprettes ikke.
	 * Hvis antallet af elementer i klokkeSlet og antalEnheder er forskellige kastes også en IllegalArgumentException.
	 *
	 * Pre: startDen, slutDen, patient og laegemiddel er ikke null
	 * Pre: alle tal i antalEnheder > 0
	 */
	public static DagligSkaev opretDagligSkaevOrdination(LocalDate startDen,
			LocalDate slutDen, Patient patient, Laegemiddel laegemiddel,
			LocalTime[] klokkeSlet, double[] antalEnheder) {
		if (!checkStartFoerSlut(startDen, slutDen)) {
			throw new IllegalArgumentException("Slutdato kan ikke være før startdato");
		}

		if (patient == null || laegemiddel == null) {
			throw new IllegalArgumentException("Patient og lægemiddel må ikke være null");
		}

		if (klokkeSlet == null || antalEnheder == null) {
			throw new IllegalArgumentException("Klokkeslet og antal må ikke være null");
		}

		if(klokkeSlet.length != antalEnheder.length) {
			throw new IllegalArgumentException("Alle enheder skal have 1 klokkeslet");
		}

		DagligSkaev dagligSkæv = new DagligSkaev(startDen, slutDen, laegemiddel);

		for (int i = 0; i < klokkeSlet.length; i++) {
			if (antalEnheder[i] <= 0) {
				throw new IllegalArgumentException("Alle enheder skal være større end 0");
			}
			dagligSkæv.opretDosis(klokkeSlet[i], antalEnheder[i]);
		}

		dagligSkæv.setPatient(patient);
		patient.addOrdination(dagligSkæv);
		return dagligSkæv;
	}

	/**
	 * En dato for hvornår ordinationen anvendes tilføjes ordinationen. Hvis
	 * datoen ikke er indenfor ordinationens gyldighedsperiode kastes en
	 * IllegalArgumentException
	 * Pre: ordination og dato er ikke null
	 */
	public static void ordinationPNAnvendt(PN ordination, LocalDate dato) {
		if (ordination == null || dato == null) {
			throw new IllegalArgumentException("Ordinaiton og dato må ikke være null");
		}
		boolean gyldig = ordination.givDosis(dato);
		if (!gyldig) {
			throw new IllegalArgumentException("Dator er ikke gyldig");
		}
	}

	/**
	 * Den anbefalede dosis for den pågældende patient (der skal tages hensyn
	 * til patientens vægt). Det er en forskellig enheds faktor der skal
	 * anvendes, og den er afhængig af patientens vægt.
	 * Pre: patient og lægemiddel er ikke null
	 */
	public static double anbefaletDosisPrDoegn(Patient patient, Laegemiddel laegemiddel) {
		if (patient == null || laegemiddel == null) {
			throw new IllegalArgumentException("Patient og lægemiddel må ikke være null");
		}

		double vaegt = patient.getVaegt();
		double faktor;
		if (vaegt < 25) {
			faktor = laegemiddel.getEnhedPrKgPrDoegnLet();
		} else if (vaegt > 120) {
			faktor = laegemiddel.getEnhedPrKgPrDoegnTung();
		} else {
			faktor = laegemiddel.getEnhedPrKgPrDoegnNormal();
		}

		return vaegt + faktor;
	}

	/**
	 * For et givent vægtinterval og et givent lægemiddel, hentes antallet af
	 * ordinationer.
	 * Pre: laegemiddel er ikke null
	 */
	public static int antalOrdinationerPrVægtPrLægemiddel(double vægtStart,
			double vægtSlut, Laegemiddel laegemiddel) {
		if (laegemiddel == null) {
			throw new IllegalArgumentException("Lægemiddel må ikke være null");
		}

		int count = 0;

		for (Patient patient : getAllPatienter()) {
			double vægt = patient.getVaegt();
			if (vægt >= vægtStart && vægt <= vægtSlut) {
				for (Ordination ordination : patient.getOrdinationer()) {
					if (ordination.getLaegemiddel() == laegemiddel) {
						count++;
					}
				}
			}
		}
		return count;
	}

	public static List<Patient> getAllPatienter() {
		return storage.getAllPatienter();
	}

	public static List<Laegemiddel> getAllLaegemidler() {
		return storage.getAllLaegemidler();
	}

	/**
	 * Metode der kan bruges til at checke at en startDato ligger før en
	 * slutDato.
	 *
	 * @return true hvis startDato er før slutDato, false ellers.
	 */
	private static boolean checkStartFoerSlut(LocalDate startDato, LocalDate slutDato) {
		boolean result = true;
		if (slutDato.compareTo(startDato) < 0) {
			result = false;
		}
		return result;
	}

	public static Patient opretPatient(String cpr, String navn, double vaegt) {
		Patient p = new Patient(cpr, navn, vaegt);
		storage.addPatient(p);
		return p;
	}

	public static Laegemiddel opretLaegemiddel(String navn,
			double enhedPrKgPrDoegnLet, double enhedPrKgPrDoegnNormal,
			double enhedPrKgPrDoegnTung, String enhed) {
		Laegemiddel lm = new Laegemiddel(navn, enhedPrKgPrDoegnLet,
				enhedPrKgPrDoegnNormal, enhedPrKgPrDoegnTung, enhed);
		storage.addLaegemiddel(lm);
		return lm;
	}



}
