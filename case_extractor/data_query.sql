\COPY (select cd.id as case_data_id, cd.create_date as case_data_create_date, cd.creator_id as case_data_creator_id, cd.deleted as case_data_deleted,    cd.last_modify_change_date as case_data_last_modify_change_date, cd.last_modify_user_id as case_data_last_modify_user_id,    cd.organisation_id as case_data_organisation_id, cd.data_rejestracji, cd.etap_procesu, cd.inicjator_procesu, cd.numer_kontraktu,    cd.numer_sprawy, cd.numer_umowy, cd.opis_przedmiotu_mediacji, cd.process_id, cd.sprawa_przekazana_do_obslugi_zewnetrznej,   cd.status_techniczny, cd.typ_sprawy, cd.umowa_indywidualna, cd.waluta_kredytu, cd.wartosc_kredytu, cd.wnioskujacy_klient,   cd.calculation_summary_id as case_data_calculation_summary_id, cd.client_interaction_summary_id as case_data_client_interaction_summary_id,    cd.finalization_parameters_id as case_data_finalization_parameters_id, cd.knf_data_id as case_data_knf_data_id,    cd.mediation_outcome_id as case_data_mediation_outcome_id, cd.przypisany_uzytkownik_id as case_data_przypisany_uzytkownik_id,   cd.credit_data_id as case_data_credit_data_id, cd.documentation_data_id as case_data_documentation_data_id,    cd.credit_decision_id as case_data_credit_decision_id, cd.godzina_rejestracji, cd.date_close_case, cd.cloned_case_id,    cd.knf_mediator_id as case_data_knf_mediator_id, cd.status_koncowy_knf, cd.court_meeting_date as case_data_court_meeting_date,   cd.court_meeting_time as case_data_court_meeting_time,      calculation.id as calculation_id, calculation.create_date as calculation_create_date, calculation.creator_id as calculation_creator_id,    calculation.deleted as calculation_deleted, calculation.last_modify_change_date as calculation_last_modify_change_date,   calculation.last_modify_user_id as calculation_last_modify_user_id, calculation.organisation_id as calculation_organisation_id,    calculation.kwota_umorzenia, calculation.marza_kredytu, calculation.ostateczny_termin_splaty_kredytu,   calculation.pozostaly_kapital_do_splatypln, calculation.procentowy_wzrost_obnizenie_wysokosci_raty, calculation.stala_stopa_procentowa,   calculation.szacunkowa_wysokosc_ostatniej_zaplaconej_raty_przy_stalym_oproc,   calculation.szacunkowa_wysokosc_ostatniej_zaplaconej_raty_przy_zmiennym_opr, calculation.wysokosc_marzy_stala_stopa_procentowa,    calculation.wysokosc_marzy_zmienne_oprocentowanie, calculation.wysokosc_oprocentowania_pierwszej_raty_po_uruchomieniuzkalkulac,    calculation.wysokosc_pierwszegowiboruzkalkulacji, calculation.notatka as calculation_notatki,      case_mediator.id as case_mediator_id, case_mediator.create_date as case_mediator_create_date, case_mediator.creator_id as case_mediator_creator_id,    case_mediator.deleted as case_mediator_deleted, case_mediator.last_modify_change_date as case_mediator_last_modify_change_date,    case_mediator.last_modify_user_id as case_mediator_last_modify_user_id, case_mediator.organisation_id as case_mediator_organisation_id,   case_mediator.adres_mailowy as mediator_mail, case_mediator.mediator_imie, case_mediator.mediator_nazwisko,    case_mediator.numer_telefonu_komorkowy as mediator_telefon, case_mediator.case_id as case_mediator_case_id,   case_mediator.knf_mediator_id as case_mediator_knf_mediator_id,      summ.id as summ_id, summ.create_date as summ_create_date, summ.creator_id as summ_creator_id, summ.deleted as summ_deleted,    summ.last_modify_change_date as summ_last_modify_change_date, summ.last_modify_user_id as summ_last_modify_user_id,    summ.organisation_id as summ_organisation_id,   summ.data_pozytywnej_rozmowyzklientem_firma_zewnetrzna, summ.data_pozytywnej_rozmowyzklientem_rbi, summ.data_wyslania_one_pager,   summ.liczba_kontaktowzklientem_firma_zewnetrzna, summ.liczba_kontaktowzklientem_rbi, summ.liczba_prob_kontaktuzklientem_firma_zewnetrzna,    summ.liczba_prob_kontaktuzklientem_rbi, summ.status_dokumentacji_klienta, summ.status_rozmowyzklientem_firma_zewnetrzna,    summ.status_rozmowyzklientem_rbi, summ.case_id as summ_case_id, summ.przyczyna_odmowy,         cr_decision.id as cr_decision_id, cr_decision.create_date as cr_decision_create_date, cr_decision.creator_id as cr_decision_creator_id, cr_decision.deleted as cr_decision_deleted,   cr_decision.last_modify_change_date as cr_decision_last_modify_change_date, cr_decision.last_modify_user_id as cr_decision_last_modify_user_id,   cr_decision.organisation_id as cr_decision_organisation_id, cr_decision.case_id as cr_decision_case_id,    cr_decision.przypisany_uzytkownik_id as cr_decision_przypisany_uzytkownik_id, cr_decision.numer_kontraktu as cr_decision_numer_kontraktu,   cr_decision.numer_sprawy as cr_decision_numer_sprawy, cr_decision.pozostaly_kapital_do_splatypln as cr_decision_pozostaly_kapital_do_splatypln,   cr_decision.kwota_umorzenia as cr_decision_kwota_umorzenia, cr_decision.ostateczny_termin_splaty_kredytu as cr_decision_ostateczny_termin_splaty_kredytu,   cr_decision.marza_kredytu as cr_decision_marza_kredytu,    cr_decision.wysokosc_oprocentowania_pierwszej_raty_po_uruchomieniu as cr_decision_wysok_oprocent_pierwszej_raty_po_uruchomieniu,   cr_decision.wysokosc_pierwszegowiboruzkalkulacji as cr_decision_wysokosc_pierwszegowiboruzkalkulacji,   cr_decision.wysokosc_marzy_zmienne_oprocentowanie as cr_decision_wysokosc_marzy_zmienne_oprocentowanie,   cr_decision.szacunkowa_wysokosc_ostatniej_zaplaconej_raty_przy_zmiennym_opr as cr_decision_szac_wysok_ostat_zaplac_raty_przy_zmiennym_opr,   cr_decision.procentowy_wzrost_obnizenie_wysokosci_raty as cr_decision_procentowy_wzrost_obnizenie_wysokosci_raty,   cr_decision.wysokosc_marzy_stala_stopa_procentowa as cr_decision_wysokosc_marzy_stala_stopa_procentowa,   cr_decision.stala_stopa_procentowa as cr_decision_stala_stopa_procentowa,    cr_decision.szacunkowa_wysokosc_ostatniej_zaplaconej_raty_przy_stalym_oproc as cr_decision_szac_wysok_ostat_zaplac_raty_przy_stalym_oproc,   cr_decision.id_klienta as cr_decision_id_klienta,      documentation.id as documentation_id, documentation.create_date as documentation_create_date, documentation.creator_id as documentation_creator_id, documentation.deleted as documentation_deleted,   documentation.last_modify_change_date as documentation_last_modify_change_date, documentation.last_modify_user_id as documentation_last_modify_user_id,   documentation.organisation_id as documentation_organisation_id,   documentation.data_waznosci_decyzji as doc_data_waznosci_decyzji, documentation.status_decyzji as doc_status_decyzji,    documentation.warunki_decyzji as doc_warunki_decyzji, documentation.data_wplywu_umowy_mediacyjnej,   documentation.kompletnosc_umowy_mediacyjnej, documentation.numer_dyspozycji, documentation.data_wysylki_umowy_mediacyjnej_do_knf,    documentation.wymagana_data_przygotowania_kalkulacji_przed_mediacja, documentation.data_kalkulacji_przed_mediacja,    documentation.wymagana_data_przygotowania_dokumentacji_przed_mediacja, documentation.data_przygotowania_dokumentow_do_mediacji,   documentation.data_przekazania_dokumentacji_do_knf_klienta_pelnomocnika, documentation.data_pozytywnego_wyniku_mediacji,    documentation.data_przygotowania_ostatecznej_kalkulacji, documentation.data_przygotowania_ostatecznej_dokumentacji,    documentation.data_wysylki_dokumentow_kurierem, documentation.data_wysylki_dokumentow_r_net,    documentation.data_koniecznego_przypomnienia_o_zwrocie_dokumentow, documentation.data_otrzymania_podpisanych_dokumentow,    documentation.czy_dokumenty_kompletne, documentation.planowana_data_realizacji, documentation.data_realizacji_w_systemie,    documentation.data_odeslania_umowy_do_knf, documentation.data_odeslania_umowy_do_klienta, documentation.case_id as documentation_case_id,   documentation.data_zawarcia_ugody as doc_data_zawarcia_ugody,      finalization.id as finalization_id, finalization.create_date as finalization_create_date, finalization.creator_id as finalization_creator_id, finalization.deleted as finalization_deleted,   finalization.last_modify_change_date as finalization_last_modify_change_date, finalization.last_modify_user_id as finalization_last_modify_user_id,   finalization.organisation_id as finalization_organisation_id,   finalization.amountwo, finalization.data_kursunbp as final_data_kursunbp, finalization.kursnbp,   finalization.outstanding_balance_after_the_settlement_inpln, finalization.outstanding_balance_before_settlement_inchf,    finalization.outstanding_balance_before_settlement_inpln, finalization.percentagewo, finalization.risk_group, finalization.case_id as finalization_case_id,      knf.id as knf_id, knf.create_date as knf_create_date, knf.creator_id as knf_creator_id, knf.deleted as knf_deleted,   knf.last_modify_change_date as knf_last_modify_change_date, knf.last_modify_user_id as knf_last_modify_user_id,   knf.organisation_id as knf_organisation_id,   knf.data_przekazania_umowy_do_knf, knf.sygnatura_sprawy, knf.techniczny_identyfikator_sprawywknf, knf.case_id as knf_case_id,      cm.id as cm_id, cm.create_date as cm_create_date, cm.creator_id as cm_creator_id, cm.deleted as cm_deleted,   cm.last_modify_change_date as cm_last_modify_change_date, cm.last_modify_user_id as cm_last_modify_user_id,   cm.organisation_id as cm_organisation_id, cm.adres_mailowy as cm_adres_mailowy, cm.mediator_imie as cm_mediator_imie,   cm.mediator_nazwisko as cm_mediator_nazwisko, cm.numer_telefonu_komorkowy as cm_numer_telefonu_komorkowy,   cm.case_id as cm_case_id, cm.knf_mediator_id as cm_knf_mediator_id,      mediation.id as mediation_id, mediation.create_date as mediation_create_date, mediation.creator_id as mediation_creator_id, mediation.deleted as mediation_deleted,   mediation.last_modify_change_date as mediation_last_modify_change_date, mediation.last_modify_user_id as mediation_last_modify_user_id,   mediation.organisation_id as mediation_organisation_id,   mediation.data_przekazania_decyzji_pozytywnej as mediation_data_przekazania_decyzji_pozytywnej, mediation.decyzja_klienta as mediation_decyzja_klienta,   mediation.rodzaj_ugody as mediation_rodzaj_ugody, mediation.wynegocjowana_kwota_do_splaty as mediation_wynegocjowana_kwota_do_splaty,   mediation.wynegocjowana_marza as mediation_wynegocjowana_marza, mediation.wynegocjowane_warunki as mediation_wynegocjowane_warunki, mediation.case_id as mediation_case_id, mediation.data_przekazania_decyzji_negatywnej as mediation_data_przekazania_decyzji_negatywnej, attorney.id as attorney_id, attorney.create_date as attorney_create_date, attorney.creator_id as attorney_creator_id, attorney.deleted as attorney_deleted, attorney.last_modify_change_date as attorney_last_modify_change_date, attorney.last_modify_user_id as attorney_last_modify_user_id, attorney.organisation_id as attorney_organisation_id, attorney.imieinazwisko1 as attorney_imieinazwisko1, attorney.imieinazwisko2 as attorney_imieinazwisko2, attorney.adres_mailowy as attorney_adres_mailowy, attorney.numer_telefonu_kmorkowy as attorney_numer_telefonu_kmorkowy from mediator.case_data cd left join mediator.case_organization_attorneys coa1_ on cd.id = coa1_.case_id left join mediator.organization_attorneys attorney on coa1_.organization_attorney_id = attorney.id left join mediator.client_interaction_summary summ on cd.id = summ.case_id left join mediator.case_mediators case_mediator on cd.id = case_mediator.case_id left join mediator.mediation_outcomes mediation on cd.id = mediation.case_id left join mediator.documentation_data documentation on cd.id = documentation.case_id left join mediator.finalization_parameters finalization on cd.id = finalization.case_id left join mediator.knf_data knf on cd.id = knf.case_id left join mediator.calculation_summaries calculation on cd.id = calculation.case_id left join mediator.credit_decisions cr_decision on cd.id = cr_decision.case_id left join mediator.case_mediators cm on cd.id = cm.case_id where cd.numer_kontraktu = 'CONTRACT_NUMBER') TO STDOUT WITH CSV HEADER