package fr.pyjacpp.diakoluo;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;

public class DiakoluoApplication extends Application {
    private ArrayList<Test> listTest;
    private Test currentTest;

    @Override
    public void onCreate() {
        super.onCreate();

        listTest = new ArrayList<>();

        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("Base verbale", "La base verbale du verbe", ColumnInputType.String));
        columns.add(new Column("Prétérit", "Le prétérit du verbe", ColumnInputType.String));
        columns.add(new Column("Participe passé", "Le participe passé du verbe", ColumnInputType.String));
        columns.add(new Column("Traduction", "La traduction du verbe", ColumnInputType.String));

        ArrayList<DataRow> dataRows = new ArrayList<>();

        dataRows.add(new DataRow());
        dataRows.get(0).getListCells().put(columns.get(0), new DataCellString("be"));
        dataRows.get(0).getListCells().put(columns.get(1), new DataCellString("was/were"));
        dataRows.get(0).getListCells().put(columns.get(2), new DataCellString("been"));
        dataRows.get(0).getListCells().put(columns.get(3), new DataCellString("être"));

        dataRows.add(new DataRow());
        dataRows.get(1).getListCells().put(columns.get(0), new DataCellString("beat"));
        dataRows.get(1).getListCells().put(columns.get(1), new DataCellString("beat"));
        dataRows.get(1).getListCells().put(columns.get(2), new DataCellString("beaten"));
        dataRows.get(1).getListCells().put(columns.get(3), new DataCellString("battre"));

        dataRows.add(new DataRow());
        dataRows.get(2).getListCells().put(columns.get(0), new DataCellString("become"));
        dataRows.get(2).getListCells().put(columns.get(1), new DataCellString("became"));
        dataRows.get(2).getListCells().put(columns.get(2), new DataCellString("become"));
        dataRows.get(2).getListCells().put(columns.get(3), new DataCellString("devenir"));

        dataRows.add(new DataRow());
        dataRows.get(3).getListCells().put(columns.get(0), new DataCellString("begin"));
        dataRows.get(3).getListCells().put(columns.get(1), new DataCellString("began"));
        dataRows.get(3).getListCells().put(columns.get(2), new DataCellString("begun"));
        dataRows.get(3).getListCells().put(columns.get(3), new DataCellString("commencer"));

        dataRows.add(new DataRow());
        dataRows.get(4).getListCells().put(columns.get(0), new DataCellString("bet"));
        dataRows.get(4).getListCells().put(columns.get(1), new DataCellString("bet"));
        dataRows.get(4).getListCells().put(columns.get(2), new DataCellString("bet"));
        dataRows.get(4).getListCells().put(columns.get(3), new DataCellString("parier"));

        dataRows.add(new DataRow());
        dataRows.get(5).getListCells().put(columns.get(0), new DataCellString("bind"));
        dataRows.get(5).getListCells().put(columns.get(1), new DataCellString("bound"));
        dataRows.get(5).getListCells().put(columns.get(2), new DataCellString("bound"));
        dataRows.get(5).getListCells().put(columns.get(3), new DataCellString("relier (un livre)"));

        dataRows.add(new DataRow());
        dataRows.get(6).getListCells().put(columns.get(0), new DataCellString("bite"));
        dataRows.get(6).getListCells().put(columns.get(1), new DataCellString("bit"));
        dataRows.get(6).getListCells().put(columns.get(2), new DataCellString("bitten"));
        dataRows.get(6).getListCells().put(columns.get(3), new DataCellString("mordre"));

        dataRows.add(new DataRow());
        dataRows.get(7).getListCells().put(columns.get(0), new DataCellString("bleed"));
        dataRows.get(7).getListCells().put(columns.get(1), new DataCellString("bled"));
        dataRows.get(7).getListCells().put(columns.get(2), new DataCellString("bled"));
        dataRows.get(7).getListCells().put(columns.get(3), new DataCellString("saigner"));

        dataRows.add(new DataRow());
        dataRows.get(8).getListCells().put(columns.get(0), new DataCellString("blow (blow one’s nose)"));
        dataRows.get(8).getListCells().put(columns.get(1), new DataCellString("blew"));
        dataRows.get(8).getListCells().put(columns.get(2), new DataCellString("blown"));
        dataRows.get(8).getListCells().put(columns.get(3), new DataCellString("souffler (se moucher)"));

        dataRows.add(new DataRow());
        dataRows.get(9).getListCells().put(columns.get(0), new DataCellString("break"));
        dataRows.get(9).getListCells().put(columns.get(1), new DataCellString("broke"));
        dataRows.get(9).getListCells().put(columns.get(2), new DataCellString("broken"));
        dataRows.get(9).getListCells().put(columns.get(3), new DataCellString("casser"));

        dataRows.add(new DataRow());
        dataRows.get(10).getListCells().put(columns.get(0), new DataCellString("bring"));
        dataRows.get(10).getListCells().put(columns.get(1), new DataCellString("brought"));
        dataRows.get(10).getListCells().put(columns.get(2), new DataCellString("brought"));
        dataRows.get(10).getListCells().put(columns.get(3), new DataCellString("apporter"));

        dataRows.add(new DataRow());
        dataRows.get(11).getListCells().put(columns.get(0), new DataCellString("build"));
        dataRows.get(11).getListCells().put(columns.get(1), new DataCellString("built"));
        dataRows.get(11).getListCells().put(columns.get(2), new DataCellString("built"));
        dataRows.get(11).getListCells().put(columns.get(3), new DataCellString("construire"));

        dataRows.add(new DataRow());
        dataRows.get(12).getListCells().put(columns.get(0), new DataCellString("burn"));
        dataRows.get(12).getListCells().put(columns.get(1), new DataCellString("burnt"));
        dataRows.get(12).getListCells().put(columns.get(2), new DataCellString("burnt"));
        dataRows.get(12).getListCells().put(columns.get(3), new DataCellString("brûler"));

        dataRows.add(new DataRow());
        dataRows.get(13).getListCells().put(columns.get(0), new DataCellString("burst (burst into)"));
        dataRows.get(13).getListCells().put(columns.get(1), new DataCellString("burst"));
        dataRows.get(13).getListCells().put(columns.get(2), new DataCellString("burst"));
        dataRows.get(13).getListCells().put(columns.get(3), new DataCellString("éclater (faire irruption)"));

        dataRows.add(new DataRow());
        dataRows.get(14).getListCells().put(columns.get(0), new DataCellString("buy"));
        dataRows.get(14).getListCells().put(columns.get(1), new DataCellString("bought"));
        dataRows.get(14).getListCells().put(columns.get(2), new DataCellString("bought"));
        dataRows.get(14).getListCells().put(columns.get(3), new DataCellString("acheter"));

        dataRows.add(new DataRow());
        dataRows.get(15).getListCells().put(columns.get(0), new DataCellString("catch"));
        dataRows.get(15).getListCells().put(columns.get(1), new DataCellString("caught"));
        dataRows.get(15).getListCells().put(columns.get(2), new DataCellString("caught"));
        dataRows.get(15).getListCells().put(columns.get(3), new DataCellString("attraper"));

        dataRows.add(new DataRow());
        dataRows.get(16).getListCells().put(columns.get(0), new DataCellString("choose"));
        dataRows.get(16).getListCells().put(columns.get(1), new DataCellString("chose"));
        dataRows.get(16).getListCells().put(columns.get(2), new DataCellString("chosen"));
        dataRows.get(16).getListCells().put(columns.get(3), new DataCellString("choisir"));

        dataRows.add(new DataRow());
        dataRows.get(17).getListCells().put(columns.get(0), new DataCellString("come"));
        dataRows.get(17).getListCells().put(columns.get(1), new DataCellString("came"));
        dataRows.get(17).getListCells().put(columns.get(2), new DataCellString("come"));
        dataRows.get(17).getListCells().put(columns.get(3), new DataCellString("venir"));

        dataRows.add(new DataRow());
        dataRows.get(18).getListCells().put(columns.get(0), new DataCellString("cost"));
        dataRows.get(18).getListCells().put(columns.get(1), new DataCellString("cost"));
        dataRows.get(18).getListCells().put(columns.get(2), new DataCellString("cost"));
        dataRows.get(18).getListCells().put(columns.get(3), new DataCellString("coûter"));

        dataRows.add(new DataRow());
        dataRows.get(19).getListCells().put(columns.get(0), new DataCellString("cut"));
        dataRows.get(19).getListCells().put(columns.get(1), new DataCellString("cut"));
        dataRows.get(19).getListCells().put(columns.get(2), new DataCellString("cut"));
        dataRows.get(19).getListCells().put(columns.get(3), new DataCellString("couper"));

        dataRows.add(new DataRow());
        dataRows.get(20).getListCells().put(columns.get(0), new DataCellString("do"));
        dataRows.get(20).getListCells().put(columns.get(1), new DataCellString("did"));
        dataRows.get(20).getListCells().put(columns.get(2), new DataCellString("done"));
        dataRows.get(20).getListCells().put(columns.get(3), new DataCellString("faire"));

        dataRows.add(new DataRow());
        dataRows.get(21).getListCells().put(columns.get(0), new DataCellString("draw"));
        dataRows.get(21).getListCells().put(columns.get(1), new DataCellString("drew"));
        dataRows.get(21).getListCells().put(columns.get(2), new DataCellString("drown"));
        dataRows.get(21).getListCells().put(columns.get(3), new DataCellString("dessiner"));

        dataRows.add(new DataRow());
        dataRows.get(22).getListCells().put(columns.get(0), new DataCellString("dream"));
        dataRows.get(22).getListCells().put(columns.get(1), new DataCellString("dreamt"));
        dataRows.get(22).getListCells().put(columns.get(2), new DataCellString("dreamt"));
        dataRows.get(22).getListCells().put(columns.get(3), new DataCellString("rêver"));

        dataRows.add(new DataRow());
        dataRows.get(23).getListCells().put(columns.get(0), new DataCellString("drink"));
        dataRows.get(23).getListCells().put(columns.get(1), new DataCellString("drank"));
        dataRows.get(23).getListCells().put(columns.get(2), new DataCellString("drunk"));
        dataRows.get(23).getListCells().put(columns.get(3), new DataCellString("boire"));

        dataRows.add(new DataRow());
        dataRows.get(24).getListCells().put(columns.get(0), new DataCellString("drive"));
        dataRows.get(24).getListCells().put(columns.get(1), new DataCellString("drove"));
        dataRows.get(24).getListCells().put(columns.get(2), new DataCellString("driven"));
        dataRows.get(24).getListCells().put(columns.get(3), new DataCellString("conduire"));

        dataRows.add(new DataRow());
        dataRows.get(25).getListCells().put(columns.get(0), new DataCellString("eat"));
        dataRows.get(25).getListCells().put(columns.get(1), new DataCellString("ate"));
        dataRows.get(25).getListCells().put(columns.get(2), new DataCellString("eaten"));
        dataRows.get(25).getListCells().put(columns.get(3), new DataCellString("manger"));

        dataRows.add(new DataRow());
        dataRows.get(26).getListCells().put(columns.get(0), new DataCellString("fall"));
        dataRows.get(26).getListCells().put(columns.get(1), new DataCellString("fell"));
        dataRows.get(26).getListCells().put(columns.get(2), new DataCellString("fallen"));
        dataRows.get(26).getListCells().put(columns.get(3), new DataCellString("tomber"));

        dataRows.add(new DataRow());
        dataRows.get(27).getListCells().put(columns.get(0), new DataCellString("feed"));
        dataRows.get(27).getListCells().put(columns.get(1), new DataCellString("fed"));
        dataRows.get(27).getListCells().put(columns.get(2), new DataCellString("fed"));
        dataRows.get(27).getListCells().put(columns.get(3), new DataCellString("nourrir"));

        dataRows.add(new DataRow());
        dataRows.get(28).getListCells().put(columns.get(0), new DataCellString("feel"));
        dataRows.get(28).getListCells().put(columns.get(1), new DataCellString("felt"));
        dataRows.get(28).getListCells().put(columns.get(2), new DataCellString("felt"));
        dataRows.get(28).getListCells().put(columns.get(3), new DataCellString("se sentir, ressentir"));

        dataRows.add(new DataRow());
        dataRows.get(29).getListCells().put(columns.get(0), new DataCellString("fight"));
        dataRows.get(29).getListCells().put(columns.get(1), new DataCellString("fought"));
        dataRows.get(29).getListCells().put(columns.get(2), new DataCellString("fought"));
        dataRows.get(29).getListCells().put(columns.get(3), new DataCellString("se battre"));

        dataRows.add(new DataRow());
        dataRows.get(30).getListCells().put(columns.get(0), new DataCellString("find"));
        dataRows.get(30).getListCells().put(columns.get(1), new DataCellString("found"));
        dataRows.get(30).getListCells().put(columns.get(2), new DataCellString("found"));
        dataRows.get(30).getListCells().put(columns.get(3), new DataCellString("trouver"));

        dataRows.add(new DataRow());
        dataRows.get(31).getListCells().put(columns.get(0), new DataCellString("fly"));
        dataRows.get(31).getListCells().put(columns.get(1), new DataCellString("flew"));
        dataRows.get(31).getListCells().put(columns.get(2), new DataCellString("flown"));
        dataRows.get(31).getListCells().put(columns.get(3), new DataCellString("voler"));

        dataRows.add(new DataRow());
        dataRows.get(32).getListCells().put(columns.get(0), new DataCellString("forget"));
        dataRows.get(32).getListCells().put(columns.get(1), new DataCellString("forgot"));
        dataRows.get(32).getListCells().put(columns.get(2), new DataCellString("forgotten"));
        dataRows.get(32).getListCells().put(columns.get(3), new DataCellString("oublier"));

        dataRows.add(new DataRow());
        dataRows.get(33).getListCells().put(columns.get(0), new DataCellString("forgive"));
        dataRows.get(33).getListCells().put(columns.get(1), new DataCellString("forgave"));
        dataRows.get(33).getListCells().put(columns.get(2), new DataCellString("forgiven"));
        dataRows.get(33).getListCells().put(columns.get(3), new DataCellString("pardonner"));

        dataRows.add(new DataRow());
        dataRows.get(34).getListCells().put(columns.get(0), new DataCellString("freeze"));
        dataRows.get(34).getListCells().put(columns.get(1), new DataCellString("froze"));
        dataRows.get(34).getListCells().put(columns.get(2), new DataCellString("frozen"));
        dataRows.get(34).getListCells().put(columns.get(3), new DataCellString("geler, se figer"));

        dataRows.add(new DataRow());
        dataRows.get(35).getListCells().put(columns.get(0), new DataCellString("get (get into)"));
        dataRows.get(35).getListCells().put(columns.get(1), new DataCellString("got"));
        dataRows.get(35).getListCells().put(columns.get(2), new DataCellString("got"));
        dataRows.get(35).getListCells().put(columns.get(3), new DataCellString("obtenir (monter dans)"));

        dataRows.add(new DataRow());
        dataRows.get(36).getListCells().put(columns.get(0), new DataCellString("give"));
        dataRows.get(36).getListCells().put(columns.get(1), new DataCellString("gave"));
        dataRows.get(36).getListCells().put(columns.get(2), new DataCellString("given"));
        dataRows.get(36).getListCells().put(columns.get(3), new DataCellString("donner"));

        dataRows.add(new DataRow());
        dataRows.get(37).getListCells().put(columns.get(0), new DataCellString("go"));
        dataRows.get(37).getListCells().put(columns.get(1), new DataCellString("went"));
        dataRows.get(37).getListCells().put(columns.get(2), new DataCellString("gone"));
        dataRows.get(37).getListCells().put(columns.get(3), new DataCellString("aller"));

        dataRows.add(new DataRow());
        dataRows.get(38).getListCells().put(columns.get(0), new DataCellString("grow"));
        dataRows.get(38).getListCells().put(columns.get(1), new DataCellString("grew"));
        dataRows.get(38).getListCells().put(columns.get(2), new DataCellString("grown"));
        dataRows.get(38).getListCells().put(columns.get(3), new DataCellString("grandir"));

        dataRows.add(new DataRow());
        dataRows.get(39).getListCells().put(columns.get(0), new DataCellString("hang (hang out)"));
        dataRows.get(39).getListCells().put(columns.get(1), new DataCellString("hung"));
        dataRows.get(39).getListCells().put(columns.get(2), new DataCellString("hung"));
        dataRows.get(39).getListCells().put(columns.get(3), new DataCellString("pendre, suspendre (traîner)"));

        dataRows.add(new DataRow());
        dataRows.get(40).getListCells().put(columns.get(0), new DataCellString("have"));
        dataRows.get(40).getListCells().put(columns.get(1), new DataCellString("had"));
        dataRows.get(40).getListCells().put(columns.get(2), new DataCellString("had"));
        dataRows.get(40).getListCells().put(columns.get(3), new DataCellString("avoir"));

        dataRows.add(new DataRow());
        dataRows.get(41).getListCells().put(columns.get(0), new DataCellString("hear"));
        dataRows.get(41).getListCells().put(columns.get(1), new DataCellString("heard"));
        dataRows.get(41).getListCells().put(columns.get(2), new DataCellString("heard"));
        dataRows.get(41).getListCells().put(columns.get(3), new DataCellString("entendre"));

        dataRows.add(new DataRow());
        dataRows.get(42).getListCells().put(columns.get(0), new DataCellString("hide"));
        dataRows.get(42).getListCells().put(columns.get(1), new DataCellString("hid"));
        dataRows.get(42).getListCells().put(columns.get(2), new DataCellString("hidden"));
        dataRows.get(42).getListCells().put(columns.get(3), new DataCellString("cacher"));

        dataRows.add(new DataRow());
        dataRows.get(43).getListCells().put(columns.get(0), new DataCellString("hit"));
        dataRows.get(43).getListCells().put(columns.get(1), new DataCellString("hit"));
        dataRows.get(43).getListCells().put(columns.get(2), new DataCellString("hit"));
        dataRows.get(43).getListCells().put(columns.get(3), new DataCellString("frapper"));

        dataRows.add(new DataRow());
        dataRows.get(44).getListCells().put(columns.get(0), new DataCellString("hold"));
        dataRows.get(44).getListCells().put(columns.get(1), new DataCellString("held"));
        dataRows.get(44).getListCells().put(columns.get(2), new DataCellString("held"));
        dataRows.get(44).getListCells().put(columns.get(3), new DataCellString("tenir"));

        dataRows.add(new DataRow());
        dataRows.get(45).getListCells().put(columns.get(0), new DataCellString("hurt"));
        dataRows.get(45).getListCells().put(columns.get(1), new DataCellString("hurt"));
        dataRows.get(45).getListCells().put(columns.get(2), new DataCellString("hurt"));
        dataRows.get(45).getListCells().put(columns.get(3), new DataCellString("blesser, faire mal, avoir mal"));

        dataRows.add(new DataRow());
        dataRows.get(46).getListCells().put(columns.get(0), new DataCellString("keep (keep going)"));
        dataRows.get(46).getListCells().put(columns.get(1), new DataCellString("kept"));
        dataRows.get(46).getListCells().put(columns.get(2), new DataCellString("kept"));
        dataRows.get(46).getListCells().put(columns.get(3), new DataCellString("garder (continuer)"));

        dataRows.add(new DataRow());
        dataRows.get(47).getListCells().put(columns.get(0), new DataCellString("know"));
        dataRows.get(47).getListCells().put(columns.get(1), new DataCellString("knew"));
        dataRows.get(47).getListCells().put(columns.get(2), new DataCellString("known"));
        dataRows.get(47).getListCells().put(columns.get(3), new DataCellString("savoir, connaître"));

        dataRows.add(new DataRow());
        dataRows.get(48).getListCells().put(columns.get(0), new DataCellString("lay (lay the table)"));
        dataRows.get(48).getListCells().put(columns.get(1), new DataCellString("laid"));
        dataRows.get(48).getListCells().put(columns.get(2), new DataCellString("laid"));
        dataRows.get(48).getListCells().put(columns.get(3), new DataCellString("poser (mettre la table)"));

        dataRows.add(new DataRow());
        dataRows.get(49).getListCells().put(columns.get(0), new DataCellString("learn"));
        dataRows.get(49).getListCells().put(columns.get(1), new DataCellString("learn"));
        dataRows.get(49).getListCells().put(columns.get(2), new DataCellString("learn"));
        dataRows.get(49).getListCells().put(columns.get(3), new DataCellString("apprendre"));

        dataRows.add(new DataRow());
        dataRows.get(50).getListCells().put(columns.get(0), new DataCellString("leave"));
        dataRows.get(50).getListCells().put(columns.get(1), new DataCellString("left"));
        dataRows.get(50).getListCells().put(columns.get(2), new DataCellString("left"));
        dataRows.get(50).getListCells().put(columns.get(3), new DataCellString("quitter, partir, laisser"));

        dataRows.add(new DataRow());
        dataRows.get(51).getListCells().put(columns.get(0), new DataCellString("lend (lend a hand/an ear)"));
        dataRows.get(51).getListCells().put(columns.get(1), new DataCellString("lent"));
        dataRows.get(51).getListCells().put(columns.get(2), new DataCellString("lent"));
        dataRows.get(51).getListCells().put(columns.get(3), new DataCellString("prêter (donner un coup de main/prêter une oreille attentive)"));

        dataRows.add(new DataRow());
        dataRows.get(52).getListCells().put(columns.get(0), new DataCellString("let (let down)"));
        dataRows.get(52).getListCells().put(columns.get(1), new DataCellString("let"));
        dataRows.get(52).getListCells().put(columns.get(2), new DataCellString("let"));
        dataRows.get(52).getListCells().put(columns.get(3), new DataCellString("permettre, laisser (laisser tomber)"));

        dataRows.add(new DataRow());
        dataRows.get(53).getListCells().put(columns.get(0), new DataCellString("lie"));
        dataRows.get(53).getListCells().put(columns.get(1), new DataCellString("lay"));
        dataRows.get(53).getListCells().put(columns.get(2), new DataCellString("lain"));
        dataRows.get(53).getListCells().put(columns.get(3), new DataCellString("s’allonger"));

        dataRows.add(new DataRow());
        dataRows.get(54).getListCells().put(columns.get(0), new DataCellString("lose"));
        dataRows.get(54).getListCells().put(columns.get(1), new DataCellString("lost"));
        dataRows.get(54).getListCells().put(columns.get(2), new DataCellString("lost"));
        dataRows.get(54).getListCells().put(columns.get(3), new DataCellString("perdre"));

        dataRows.add(new DataRow());
        dataRows.get(55).getListCells().put(columns.get(0), new DataCellString("make"));
        dataRows.get(55).getListCells().put(columns.get(1), new DataCellString("made"));
        dataRows.get(55).getListCells().put(columns.get(2), new DataCellString("made"));
        dataRows.get(55).getListCells().put(columns.get(3), new DataCellString("faire, fabriquer"));

        dataRows.add(new DataRow());
        dataRows.get(56).getListCells().put(columns.get(0), new DataCellString("mean"));
        dataRows.get(56).getListCells().put(columns.get(1), new DataCellString("meant"));
        dataRows.get(56).getListCells().put(columns.get(2), new DataCellString("meant"));
        dataRows.get(56).getListCells().put(columns.get(3), new DataCellString("vouloir dire"));

        dataRows.add(new DataRow());
        dataRows.get(57).getListCells().put(columns.get(0), new DataCellString("meet"));
        dataRows.get(57).getListCells().put(columns.get(1), new DataCellString("met"));
        dataRows.get(57).getListCells().put(columns.get(2), new DataCellString("met"));
        dataRows.get(57).getListCells().put(columns.get(3), new DataCellString("rencontrer"));

        dataRows.add(new DataRow());
        dataRows.get(58).getListCells().put(columns.get(0), new DataCellString("pay"));
        dataRows.get(58).getListCells().put(columns.get(1), new DataCellString("paid"));
        dataRows.get(58).getListCells().put(columns.get(2), new DataCellString("paid"));
        dataRows.get(58).getListCells().put(columns.get(3), new DataCellString("payer"));

        dataRows.add(new DataRow());
        dataRows.get(59).getListCells().put(columns.get(0), new DataCellString("put"));
        dataRows.get(59).getListCells().put(columns.get(1), new DataCellString("put"));
        dataRows.get(59).getListCells().put(columns.get(2), new DataCellString("put"));
        dataRows.get(59).getListCells().put(columns.get(3), new DataCellString("mettre"));

        dataRows.add(new DataRow());
        dataRows.get(60).getListCells().put(columns.get(0), new DataCellString("read"));
        dataRows.get(60).getListCells().put(columns.get(1), new DataCellString("read"));
        dataRows.get(60).getListCells().put(columns.get(2), new DataCellString("read"));
        dataRows.get(60).getListCells().put(columns.get(3), new DataCellString("lire"));

        dataRows.add(new DataRow());
        dataRows.get(61).getListCells().put(columns.get(0), new DataCellString("rent"));
        dataRows.get(61).getListCells().put(columns.get(1), new DataCellString("rent"));
        dataRows.get(61).getListCells().put(columns.get(2), new DataCellString("rent"));
        dataRows.get(61).getListCells().put(columns.get(3), new DataCellString("louer"));

        dataRows.add(new DataRow());
        dataRows.get(62).getListCells().put(columns.get(0), new DataCellString("ride"));
        dataRows.get(62).getListCells().put(columns.get(1), new DataCellString("rode"));
        dataRows.get(62).getListCells().put(columns.get(2), new DataCellString("ridden"));
        dataRows.get(62).getListCells().put(columns.get(3), new DataCellString("aller à bicyclette, à cheval"));

        dataRows.add(new DataRow());
        dataRows.get(63).getListCells().put(columns.get(0), new DataCellString("ring"));
        dataRows.get(63).getListCells().put(columns.get(1), new DataCellString("rang"));
        dataRows.get(63).getListCells().put(columns.get(2), new DataCellString("rung"));
        dataRows.get(63).getListCells().put(columns.get(3), new DataCellString("sonner"));

        dataRows.add(new DataRow());
        dataRows.get(64).getListCells().put(columns.get(0), new DataCellString("rise"));
        dataRows.get(64).getListCells().put(columns.get(1), new DataCellString("rose"));
        dataRows.get(64).getListCells().put(columns.get(2), new DataCellString("risen"));
        dataRows.get(64).getListCells().put(columns.get(3), new DataCellString("monter, s’élever"));

        dataRows.add(new DataRow());
        dataRows.get(65).getListCells().put(columns.get(0), new DataCellString("run"));
        dataRows.get(65).getListCells().put(columns.get(1), new DataCellString("ran"));
        dataRows.get(65).getListCells().put(columns.get(2), new DataCellString("run"));
        dataRows.get(65).getListCells().put(columns.get(3), new DataCellString("courir"));

        dataRows.add(new DataRow());
        dataRows.get(66).getListCells().put(columns.get(0), new DataCellString("say"));
        dataRows.get(66).getListCells().put(columns.get(1), new DataCellString("said"));
        dataRows.get(66).getListCells().put(columns.get(2), new DataCellString("said"));
        dataRows.get(66).getListCells().put(columns.get(3), new DataCellString("dire"));

        dataRows.add(new DataRow());
        dataRows.get(67).getListCells().put(columns.get(0), new DataCellString("see"));
        dataRows.get(67).getListCells().put(columns.get(1), new DataCellString("saw"));
        dataRows.get(67).getListCells().put(columns.get(2), new DataCellString("seen"));
        dataRows.get(67).getListCells().put(columns.get(3), new DataCellString("voir"));

        dataRows.add(new DataRow());
        dataRows.get(68).getListCells().put(columns.get(0), new DataCellString("sell"));
        dataRows.get(68).getListCells().put(columns.get(1), new DataCellString("sold"));
        dataRows.get(68).getListCells().put(columns.get(2), new DataCellString("sold"));
        dataRows.get(68).getListCells().put(columns.get(3), new DataCellString("vendre"));

        dataRows.add(new DataRow());
        dataRows.get(69).getListCells().put(columns.get(0), new DataCellString("send"));
        dataRows.get(69).getListCells().put(columns.get(1), new DataCellString("sent"));
        dataRows.get(69).getListCells().put(columns.get(2), new DataCellString("sent"));
        dataRows.get(69).getListCells().put(columns.get(3), new DataCellString("envoyer"));

        dataRows.add(new DataRow());
        dataRows.get(70).getListCells().put(columns.get(0), new DataCellString("set (set the table)"));
        dataRows.get(70).getListCells().put(columns.get(1), new DataCellString("set"));
        dataRows.get(70).getListCells().put(columns.get(2), new DataCellString("set"));
        dataRows.get(70).getListCells().put(columns.get(3), new DataCellString("poser, placer, mettre (la table)"));

        dataRows.add(new DataRow());
        dataRows.get(71).getListCells().put(columns.get(0), new DataCellString("shake"));
        dataRows.get(71).getListCells().put(columns.get(1), new DataCellString("shook"));
        dataRows.get(71).getListCells().put(columns.get(2), new DataCellString("shaken"));
        dataRows.get(71).getListCells().put(columns.get(3), new DataCellString("trembler"));

        dataRows.add(new DataRow());
        dataRows.get(72).getListCells().put(columns.get(0), new DataCellString("shine"));
        dataRows.get(72).getListCells().put(columns.get(1), new DataCellString("shone"));
        dataRows.get(72).getListCells().put(columns.get(2), new DataCellString("shone"));
        dataRows.get(72).getListCells().put(columns.get(3), new DataCellString("briller"));

        dataRows.add(new DataRow());
        dataRows.get(73).getListCells().put(columns.get(0), new DataCellString("shoot"));
        dataRows.get(73).getListCells().put(columns.get(1), new DataCellString("shot"));
        dataRows.get(73).getListCells().put(columns.get(2), new DataCellString("shot"));
        dataRows.get(73).getListCells().put(columns.get(3), new DataCellString("tirer, marquer, tourner (un film)"));

        dataRows.add(new DataRow());
        dataRows.get(74).getListCells().put(columns.get(0), new DataCellString("show (show off)"));
        dataRows.get(74).getListCells().put(columns.get(1), new DataCellString("showed"));
        dataRows.get(74).getListCells().put(columns.get(2), new DataCellString("shown"));
        dataRows.get(74).getListCells().put(columns.get(3), new DataCellString("montrer (se vanter)"));

        dataRows.add(new DataRow());
        dataRows.get(75).getListCells().put(columns.get(0), new DataCellString("shut"));
        dataRows.get(75).getListCells().put(columns.get(1), new DataCellString("shut"));
        dataRows.get(75).getListCells().put(columns.get(2), new DataCellString("shut"));
        dataRows.get(75).getListCells().put(columns.get(3), new DataCellString("fermer"));

        dataRows.add(new DataRow());
        dataRows.get(76).getListCells().put(columns.get(0), new DataCellString("sing"));
        dataRows.get(76).getListCells().put(columns.get(1), new DataCellString("sang"));
        dataRows.get(76).getListCells().put(columns.get(2), new DataCellString("sung"));
        dataRows.get(76).getListCells().put(columns.get(3), new DataCellString("chanter"));

        dataRows.add(new DataRow());
        dataRows.get(77).getListCells().put(columns.get(0), new DataCellString("sit (down)"));
        dataRows.get(77).getListCells().put(columns.get(1), new DataCellString("sat"));
        dataRows.get(77).getListCells().put(columns.get(2), new DataCellString("sat"));
        dataRows.get(77).getListCells().put(columns.get(3), new DataCellString("s’assoir"));

        dataRows.add(new DataRow());
        dataRows.get(78).getListCells().put(columns.get(0), new DataCellString("sleep"));
        dataRows.get(78).getListCells().put(columns.get(1), new DataCellString("slept"));
        dataRows.get(78).getListCells().put(columns.get(2), new DataCellString("slept"));
        dataRows.get(78).getListCells().put(columns.get(3), new DataCellString("dormir"));

        dataRows.add(new DataRow());
        dataRows.get(79).getListCells().put(columns.get(0), new DataCellString("smell"));
        dataRows.get(79).getListCells().put(columns.get(1), new DataCellString("smelt"));
        dataRows.get(79).getListCells().put(columns.get(2), new DataCellString("smelt"));
        dataRows.get(79).getListCells().put(columns.get(3), new DataCellString("sentir (odorat)"));

        dataRows.add(new DataRow());
        dataRows.get(80).getListCells().put(columns.get(0), new DataCellString("speak"));
        dataRows.get(80).getListCells().put(columns.get(1), new DataCellString("spoke"));
        dataRows.get(80).getListCells().put(columns.get(2), new DataCellString("spoken"));
        dataRows.get(80).getListCells().put(columns.get(3), new DataCellString("parler"));

        dataRows.add(new DataRow());
        dataRows.get(81).getListCells().put(columns.get(0), new DataCellString("spend"));
        dataRows.get(81).getListCells().put(columns.get(1), new DataCellString("spent"));
        dataRows.get(81).getListCells().put(columns.get(2), new DataCellString("spent"));
        dataRows.get(81).getListCells().put(columns.get(3), new DataCellString("dépenser (de l’argent), passer (du temps)"));

        dataRows.add(new DataRow());
        dataRows.get(82).getListCells().put(columns.get(0), new DataCellString("spread"));
        dataRows.get(82).getListCells().put(columns.get(1), new DataCellString("spread"));
        dataRows.get(82).getListCells().put(columns.get(2), new DataCellString("spread"));
        dataRows.get(82).getListCells().put(columns.get(3), new DataCellString("étaler, répandre"));

        dataRows.add(new DataRow());
        dataRows.get(83).getListCells().put(columns.get(0), new DataCellString("stand"));
        dataRows.get(83).getListCells().put(columns.get(1), new DataCellString("stood"));
        dataRows.get(83).getListCells().put(columns.get(2), new DataCellString("stood"));
        dataRows.get(83).getListCells().put(columns.get(3), new DataCellString("se tenir debout"));

        dataRows.add(new DataRow());
        dataRows.get(84).getListCells().put(columns.get(0), new DataCellString("steal"));
        dataRows.get(84).getListCells().put(columns.get(1), new DataCellString("stole"));
        dataRows.get(84).getListCells().put(columns.get(2), new DataCellString("stolen"));
        dataRows.get(84).getListCells().put(columns.get(3), new DataCellString("voler, dérober"));

        dataRows.add(new DataRow());
        dataRows.get(85).getListCells().put(columns.get(0), new DataCellString("strike (a deal)"));
        dataRows.get(85).getListCells().put(columns.get(1), new DataCellString("struck"));
        dataRows.get(85).getListCells().put(columns.get(2), new DataCellString("struck"));
        dataRows.get(85).getListCells().put(columns.get(3), new DataCellString("frapper, donner un coup (passer un accord)"));

        dataRows.add(new DataRow());
        dataRows.get(86).getListCells().put(columns.get(0), new DataCellString("swear"));
        dataRows.get(86).getListCells().put(columns.get(1), new DataCellString("swore"));
        dataRows.get(86).getListCells().put(columns.get(2), new DataCellString("sworn"));
        dataRows.get(86).getListCells().put(columns.get(3), new DataCellString("jurer"));

        dataRows.add(new DataRow());
        dataRows.get(87).getListCells().put(columns.get(0), new DataCellString("swim"));
        dataRows.get(87).getListCells().put(columns.get(1), new DataCellString("swam"));
        dataRows.get(87).getListCells().put(columns.get(2), new DataCellString("swum"));
        dataRows.get(87).getListCells().put(columns.get(3), new DataCellString("nager"));

        dataRows.add(new DataRow());
        dataRows.get(88).getListCells().put(columns.get(0), new DataCellString("take"));
        dataRows.get(88).getListCells().put(columns.get(1), new DataCellString("took"));
        dataRows.get(88).getListCells().put(columns.get(2), new DataCellString("taken"));
        dataRows.get(88).getListCells().put(columns.get(3), new DataCellString("prendre"));

        dataRows.add(new DataRow());
        dataRows.get(89).getListCells().put(columns.get(0), new DataCellString("teach"));
        dataRows.get(89).getListCells().put(columns.get(1), new DataCellString("taught"));
        dataRows.get(89).getListCells().put(columns.get(2), new DataCellString("taught"));
        dataRows.get(89).getListCells().put(columns.get(3), new DataCellString("enseigner, apprendre"));

        dataRows.add(new DataRow());
        dataRows.get(90).getListCells().put(columns.get(0), new DataCellString("tear"));
        dataRows.get(90).getListCells().put(columns.get(1), new DataCellString("tore"));
        dataRows.get(90).getListCells().put(columns.get(2), new DataCellString("torn"));
        dataRows.get(90).getListCells().put(columns.get(3), new DataCellString("déchirer"));

        dataRows.add(new DataRow());
        dataRows.get(91).getListCells().put(columns.get(0), new DataCellString("tell"));
        dataRows.get(91).getListCells().put(columns.get(1), new DataCellString("told"));
        dataRows.get(91).getListCells().put(columns.get(2), new DataCellString("told"));
        dataRows.get(91).getListCells().put(columns.get(3), new DataCellString("dire, raconter"));

        dataRows.add(new DataRow());
        dataRows.get(92).getListCells().put(columns.get(0), new DataCellString("think"));
        dataRows.get(92).getListCells().put(columns.get(1), new DataCellString("thought"));
        dataRows.get(92).getListCells().put(columns.get(2), new DataCellString("thought"));
        dataRows.get(92).getListCells().put(columns.get(3), new DataCellString("penser"));

        dataRows.add(new DataRow());
        dataRows.get(93).getListCells().put(columns.get(0), new DataCellString("throw"));
        dataRows.get(93).getListCells().put(columns.get(1), new DataCellString("threw"));
        dataRows.get(93).getListCells().put(columns.get(2), new DataCellString("thrown"));
        dataRows.get(93).getListCells().put(columns.get(3), new DataCellString("jeter, lancer"));

        dataRows.add(new DataRow());
        dataRows.get(94).getListCells().put(columns.get(0), new DataCellString("understand"));
        dataRows.get(94).getListCells().put(columns.get(1), new DataCellString("understood"));
        dataRows.get(94).getListCells().put(columns.get(2), new DataCellString("understood"));
        dataRows.get(94).getListCells().put(columns.get(3), new DataCellString("comprendre"));

        dataRows.add(new DataRow());
        dataRows.get(95).getListCells().put(columns.get(0), new DataCellString("upset"));
        dataRows.get(95).getListCells().put(columns.get(1), new DataCellString("upset"));
        dataRows.get(95).getListCells().put(columns.get(2), new DataCellString("upset"));
        dataRows.get(95).getListCells().put(columns.get(3), new DataCellString("contrarier"));

        dataRows.add(new DataRow());
        dataRows.get(96).getListCells().put(columns.get(0), new DataCellString("wake up"));
        dataRows.get(96).getListCells().put(columns.get(1), new DataCellString("woke up"));
        dataRows.get(96).getListCells().put(columns.get(2), new DataCellString("woken up"));
        dataRows.get(96).getListCells().put(columns.get(3), new DataCellString("se réveiller"));

        dataRows.add(new DataRow());
        dataRows.get(97).getListCells().put(columns.get(0), new DataCellString("wear"));
        dataRows.get(97).getListCells().put(columns.get(1), new DataCellString("wore"));
        dataRows.get(97).getListCells().put(columns.get(2), new DataCellString("worn"));
        dataRows.get(97).getListCells().put(columns.get(3), new DataCellString("porter (vêtements)"));

        dataRows.add(new DataRow());
        dataRows.get(98).getListCells().put(columns.get(0), new DataCellString("win"));
        dataRows.get(98).getListCells().put(columns.get(1), new DataCellString("won"));
        dataRows.get(98).getListCells().put(columns.get(2), new DataCellString("won"));
        dataRows.get(98).getListCells().put(columns.get(3), new DataCellString("gagner"));

        dataRows.add(new DataRow());
        dataRows.get(99).getListCells().put(columns.get(0), new DataCellString("write"));
        dataRows.get(99).getListCells().put(columns.get(1), new DataCellString("wrote"));
        dataRows.get(99).getListCells().put(columns.get(2), new DataCellString("written"));
        dataRows.get(99).getListCells().put(columns.get(3), new DataCellString("écrire"));



        listTest.add(new Test(
                "Test 1", getString(R.string.very_long_description), new Date(), new Date(), 1, columns, dataRows
        ));

        setCurrentTest(0);
    }

    public void setCurrentTest(Test currentTest) {
        this.currentTest = currentTest;
    }

    public void setCurrentTest(int currentTest) {
        this.currentTest = getListTest().get(currentTest);
    }

    public Test getCurrentTest() {
        return currentTest;
    }

    public ArrayList<Test> getListTest() {
        return listTest;
    }

    public static void setCurrentTest(Context context, Test currentTest) {
        ((DiakoluoApplication) context.getApplicationContext()).setCurrentTest(currentTest);
    }

    public static Test getCurrentTest(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getCurrentTest();
    }

    public static ArrayList<Test> getListTest(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getListTest();
    }
}
