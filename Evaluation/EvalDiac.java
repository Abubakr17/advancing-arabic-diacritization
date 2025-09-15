/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaldiac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author hmubarak
 */
public class EvalDiac {
    // Diac codes (strings)
    public static final String DIAC_CODE_EMPTY          = "0";

    public static final String DIAC_CODE_FATHA          = "1";
    public static final String DIAC_CODE_KASRA          = "2";
    public static final String DIAC_CODE_DAMMA          = "3";
    public static final String DIAC_CODE_SUKUN          = "4";
    public static final String DIAC_CODE_FATHATAN       = "5";
    public static final String DIAC_CODE_KASRATAN       = "6";
    public static final String DIAC_CODE_DAMMATAN       = "7";

    public static final String DIAC_CODE_SHADDA         = "9";

    public static final String DIAC_CODE_SHADDA_FATHA   = "A";
    public static final String DIAC_CODE_SHADDA_KASRA   = "B";
    public static final String DIAC_CODE_SHADDA_DAMMA   = "C";
    public static final String DIAC_CODE_SHADDA_FATHATAN= "D";
    public static final String DIAC_CODE_SHADDA_KASRATAN= "E";
    public static final String DIAC_CODE_SHADDA_DAMMATAN= "F";
    public static final String DIAC_CODE_SHADDA_SUKUN   = "G";
    
    // Diac codes (characters)
    public static final char CH_DIAC_CODE_EMPTY          = '0';

    public static final char CH_DIAC_CODE_FATHA          = '1';
    public static final char CH_DIAC_CODE_KASRA          = '2';
    public static final char CH_DIAC_CODE_DAMMA          = '3';
    public static final char CH_DIAC_CODE_SUKUN          = '4';
    public static final char CH_DIAC_CODE_FATHATAN       = '5';
    public static final char CH_DIAC_CODE_KASRATAN       = '6';
    public static final char CH_DIAC_CODE_DAMMATAN       = '7';

    public static final char CH_DIAC_CODE_SHADDA         = '9';

    public static final char CH_DIAC_CODE_SHADDA_FATHA   = 'A';
    public static final char CH_DIAC_CODE_SHADDA_KASRA   = 'B';
    public static final char CH_DIAC_CODE_SHADDA_DAMMA   = 'C';
    public static final char CH_DIAC_CODE_SHADDA_FATHATAN= 'D';
    public static final char CH_DIAC_CODE_SHADDA_KASRATAN= 'E';
    public static final char CH_DIAC_CODE_SHADDA_DAMMATAN= 'F';
    public static final char CH_DIAC_CODE_SHADDA_SUKUN   = 'G';

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // Example: -r d:\Wikipedia\WikiNews\WikiNewsTruth.txt.diac -s D:\Wikipedia\WikiNews\WikiNewsTruthDNNOut.txt
	int i, nofArgs;
	String arg, refFile, sysFile, msg;
	boolean stemAccuracy, consonantClustering;

        if ((args.length == 0) || (args.length != 4 && args.length != 5 && args.length != 6))
        {
            System.out.println("Evaluate the diacritization accuracy by comparing reference and system output files\nUsage: EvalDiac <--help|-h> <[-r|--ref] [refFilename]> <[-s|--sys] [sysFilename]> <[-t|--stemAccuracy] true|false> <[-c|--consonantClustering] true|false>");
            System.exit(-1);
        }

        refFile = sysFile = "";
        stemAccuracy = consonantClustering = false;
        nofArgs = 0;

        for (i = 0; i < args.length; i++)
	{
	    arg = args[i];            
            msg = String.format("arg:%d %s", i, arg);
            System.out.println(msg);
        }

        for (i = 0; i < args.length; i++)
	{
	    arg = args[i];
            
            if (arg.equals("--help") || arg.equals("-h"))
	    {
                System.out.println("Evaluate the diacritization accuracy by comparing reference and system output files\nUsage: EvalDiac <--help|-h> <[-r|--ref] [refFilename]> <[-s|--sys] [sysFilename]> <[-t|--stemAccuracy] true|false> <[-c|--consonantClustering] true|false>");
		System.exit(-1);
	    }

	    if (arg.equals("--ref") || arg.equals("-r"))
	    {
		nofArgs++;
                if (i < args.length - 1)
                {
                    refFile = args[i + 1];
                    i++;
                }
	    }
	    if (arg.equals("--sys") || arg.equals("-s"))
	    {
		nofArgs++;
                if (i < args.length - 1)
                {
                    sysFile = args[i + 1];
                    i++;
                }
	    }
	    if (arg.equals("--stemAccuracy") || arg.equals("-t"))
	    {
                // 0:for full diacritization, 1: for stem diacritization
		nofArgs++;
                if (i < args.length - 1)
                {
                    if (args[i + 1].toLowerCase().equals("true"))
                    {
                        stemAccuracy = true;
                    }
                    i++;
                }
	    }
	    if (arg.equals("--consonantClustering") || arg.equals("-c"))
	    {
                // التقاء الساكنين
		nofArgs++;
                if (i < args.length - 1)
                {
                    if (args[i + 1].toLowerCase().equals("true"))
                    {
                        consonantClustering = true;
                    }
                    i++;
                }
	    }
	}

        msg = String.format("\nReference filename:\t%s\nSystem filename:\t%s\nstem Accuracy:\t\t%b\nconsonant Clustering:\t%b\n", refFile, sysFile, stemAccuracy, consonantClustering);
        System.out.println(msg);
        
        if (!new File(refFile).isFile() || !new File(sysFile).isFile())
        {
            System.out.println("Reference or system file not found!");
            System.exit(-1);            
        }
        
        EvalDiac evalDiac = new EvalDiac();

        System.err.println("Evaluating Diacritization Accuracy...");
        evalDiac.calcDiacAccuracy(refFile, sysFile, stemAccuracy, consonantClustering);
        System.err.println("Done!");

    }


    public static int LoadFile(String filename, List<String> list)
    {
        int nofLines, dbgLineNo, breakpoint, errors, i, maxNofLines;
        String strLine, msg;

        nofLines = 0;
        dbgLineNo = 1226;
        errors = 0;
        maxNofLines = -1;

        try
        {
            list.clear();
            java.io.File file = new java.io.File(filename);
            if (file.exists())
            {
                FileInputStream fstream = new FileInputStream(filename);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

                //Read File Line By Line
                while ((strLine = br.readLine()) != null)
                {
                    if (nofLines == dbgLineNo)
                    {
                        breakpoint = 1;
                    }

                    if (nofLines == maxNofLines)
                    {
                        break;
                    }

                    if ((nofLines > 0) && (nofLines % 10000) == 0)
                    {
                        //msg = String.format("LoadFile(). Reading: %s, Line:%d", filename, nofLines);
                        //System.out.println (msg);
                    }

                    if (strLine.strip().length() == 0)
                    {
                        continue;
                    }

                    list.add(strLine);

                    nofLines++;
                }
                //Close the input stream
                br.close();
            }
            else
            {
                nofLines = -1;
            }
        }
	catch (Exception e)
        {
            e.printStackTrace();
	}
               
        return nofLines;
    }

    
    public static String removeDefaultDiac(String s)
    {
        String out;
        
        out = s;
        out = out.replaceAll("َا", "ا");
        out = out.replaceAll("ِي", "ي");
        out = out.replaceAll("ُو", "و");
        out = out.replaceAll("الْ", "ال");
        
        out = out.replaceAll("ْ", "");
        
        out = out.replaceAll("َّ", "َّ");
        out = out.replaceAll("ِّ", "ِّ");
        out = out.replaceAll("ُّ", "ُّ");
        out = out.replaceAll("ًّ", "ًّ");
        out = out.replaceAll("ٍّ", "ٍّ");
        out = out.replaceAll("ٌّ", "ٌّ");
        
        out = out.replaceAll("اَ", "ا");
        
        // التقاء الساكنين: ref:اخْتِتَامُ, sys:اِخْتِتامُ
        out = out.replaceAll("اِ", "ا");
        out = out.replaceAll("لِا", "لا");
        
        out = out.replaceAll("اً", "ًا");
        
        return out;
    }
    
    public static String getDiacCodes(String s)
    {
        char ch, ch2, ch3;
        int i, len, diac;
        String code, diacCodes;
        
        diacCodes = "";
        len = s.length();
        
        for (i = 0; i < len; i++){
            ch = s.charAt(i);
            
            diacCodes += ch;
            
            diac = 0;
            code = DIAC_CODE_EMPTY;
            
            if (i < len - 1)
            {
                ch2 = s.charAt(i + 1);
                if (ch2 == 'َ'){
                    diac = 1;
                    code = DIAC_CODE_FATHA;
                }
                else if (ch2 == 'ِ'){
                    diac = 1;
                    code = DIAC_CODE_KASRA;
                }
                else if (ch2 == 'ُ'){
                    diac = 1;
                    code = DIAC_CODE_DAMMA;
                }
                else if (ch2 == 'ْ'){
                    diac = 1;
                    code = DIAC_CODE_SUKUN;
                }
                else if (ch2 == 'ً'){
                    diac = 1;
                    code = DIAC_CODE_FATHATAN;
                }
                else if (ch2 == 'ٍ'){
                    diac = 1;
                    code = DIAC_CODE_KASRATAN;
                }
                else if (ch2 == 'ٌ'){
                    diac = 1;
                    code = DIAC_CODE_DAMMATAN;
                }
                else if (ch2 == 'ّ'){
                    diac = 1;
                    code = DIAC_CODE_SHADDA;

                    if (i < len - 2)
                    {
                        ch3 = s.charAt(i + 2);

                        if (ch3 == 'َ'){
                            diac = 2;
                            code = DIAC_CODE_SHADDA_FATHA;
                        }
                        else if (ch3 == 'ِ'){
                            diac = 2;
                            code = DIAC_CODE_SHADDA_KASRA;
                        }
                        else if (ch3 == 'ُ'){
                            diac = 2;
                            code = DIAC_CODE_SHADDA_DAMMA;
                        }
                        else if (ch3 == 'ً'){
                            diac = 2;
                            code = DIAC_CODE_SHADDA_FATHATAN;
                        }
                        else if (ch3 == 'ٍ'){
                            diac = 2;
                            code = DIAC_CODE_SHADDA_KASRATAN;
                        }
                        else if (ch3 == 'ٌ'){
                            diac = 2;
                            code = DIAC_CODE_SHADDA_DAMMATAN;
                        }                
                        else if (ch3 == 'ْ'){
                            // Error and should not happen in MSA, but it happened in MGR dialects
                            diac = 2;
                            code = DIAC_CODE_SHADDA_SUKUN;
                        }
                    }
                }
            }
            
            diacCodes += code;
            i += diac;
        }
        
        return diacCodes;
    }
    
    
    public static String restoreWordsFromDiacCodes(String s, int noCaseEnding)
    {
        char ch, code;
        int i, len, diac, end;
        String word;
        
        word = "";
        len = s.length();
        
        end = (len + noCaseEnding ) / 2;
        for (i = 0; i < end; i++){
            ch = s.charAt(i * 2);
            
            word += ch;
            
            //if ((i * 2 > end) || ((i * 2 + 1) >= len))
            if ((i * 2 + 1) >= len)
            {
                break;
            }
            code = s.charAt(i * 2 + 1);
            
            if (code == CH_DIAC_CODE_FATHA){
                word += 'َ';
            }
            else if (code == CH_DIAC_CODE_KASRA){
                word += 'ِ';
            }
            else if (code == CH_DIAC_CODE_DAMMA){
                word += 'ُ';
            }
            else if (code == CH_DIAC_CODE_SUKUN){
                word += 'ْ';
            }
            else if (code == CH_DIAC_CODE_FATHATAN){
                word += 'ً';
            }
            else if (code == CH_DIAC_CODE_KASRATAN){
                word += 'ٍ';
            }
            else if (code == CH_DIAC_CODE_DAMMATAN){
                word += 'ٌ';
            }
            else if (code == CH_DIAC_CODE_SHADDA){
                word += 'ّ';
            }
            else if (code == CH_DIAC_CODE_SHADDA_FATHA){
                word += 'ّ';
                word += 'َ';
            }
            else if (code == CH_DIAC_CODE_SHADDA_KASRA){
                word += 'ّ';
                word += 'ِ';
            }
            else if (code == CH_DIAC_CODE_SHADDA_DAMMA){
                word += 'ّ';
                word += 'ُ';
            }
            else if (code == CH_DIAC_CODE_SHADDA_FATHATAN){
                word += 'ّ';
                word += 'ً';
            }
            else if (code == CH_DIAC_CODE_SHADDA_KASRATAN){
                word += 'ّ';
                word += 'ٍ';
            }
            else if (code == CH_DIAC_CODE_SHADDA_DAMMATAN){
                word += 'ّ';
                word += 'ٌ';
            }                
            else if (code == CH_DIAC_CODE_SHADDA_SUKUN){
                word += 'ّ';
                word += 'ْ';
            }                
        }
        
        return word;
    }
    
    
    public static String normalizeWord(String t, boolean normalizeCommon, boolean normalizeHamza, boolean removeDiac) {
        String s;

        s = t;

        if (normalizeCommon)
        {
            s = s.replaceAll("أ", "ا");
            s = s.replaceAll("إ", "ا");
            s = s.replaceAll("آ", "ا");
            s = s.replaceAll("ى", "ي");
            s = s.replaceAll("ة", "ه");
            
            if (normalizeHamza)
            {
                s = s.replaceAll("ؤ", "ء");
                s = s.replaceAll("ئ", "ء");
            }
        }
        
        if (removeDiac)
        {
            s = s.replaceAll("َ", "");
            s = s.replaceAll("ُ", "");
            s = s.replaceAll("ِ", "");
            s = s.replaceAll("ّ", "");
            s = s.replaceAll("ْ", "");
            s = s.replaceAll("ٌ", "");
            s = s.replaceAll("ً", "");
            s = s.replaceAll("ٍ", "");
            
            s = s.replaceAll("ـ", "");
        }
        
        //s = removeDiacritics(s);

        return s;
    }

    
    public static void calcDiacAccuracy(String refFile, String sysFile, boolean stemAccuracy, boolean consonantClustering)
    {
        boolean breakpoint, showDiffOnly, diff, diacChanged, isMultiRefWord, gotMultiRefCorrect;
        int i, j, k, wordIndex, dbgLineNo, nofWords, nofRefLines, nofSysLines, nofLetters, correctWords, correctLetters, errors, errors2, errors3, sysIndex, refDiacCodesLen, sysDiacCodesLen, rdiDiacCodesLen, maxNofLines, calcStemAccuracy, rdiMostCommonFreq, freq, tmpErrors, nofMultiRefWords;
        char d1, d2, ch1, ch2, nextCh1, nextCh2, nextDiac1, nextDiac2;
        String r, s, s2, s3, msg, msg2, ref, sys, newSys, refDiacCodes, sysDiacCodes, sysDiacCodes2, rdiDiacCodes, value, rdiMostCommonDiac, tmpDiacCodes, diacPOS, POS, curr_ref;
        String[] fieldsRef, fieldsSys, all_refs;
        List<String> refLines = new ArrayList<String>();
        List<String> sysLines = new ArrayList<String>();
        HashMap<String, String> rdiDiacMap = new HashMap<String, String>();

        nofRefLines = LoadFile(refFile, refLines);
        nofSysLines = LoadFile(sysFile, sysLines);
        
        showDiffOnly = true;
        maxNofLines = 0;
        nofWords = 0;
        errors = 0;
        errors2 = 0;
        errors3 = 0;
        dbgLineNo = 360;
        nofLetters = 0;
        correctWords = 0;
        correctLetters = 0;
        nofMultiRefWords = 0;
        
        for (i = 0; i < nofRefLines; i++)
        {
            if ((i > 0) && (i % 10) == 0)
            {
                if (!stemAccuracy)
                {
                    msg = String.format("lines:%d/%d\twords:%d\tcorrectWords:%d\tletters:%d\tcorrectLetters:%d\tWER:%.2f%%\tDER:%.2f%%\tlineErrors:%d\twordErrors:%d", i, nofRefLines, nofWords, correctWords, nofLetters, correctLetters, 100.0 - (((float)correctWords * 100) / nofWords), 100.0 - (((float)correctLetters * 100) / nofLetters), errors, errors2);
                }
                else
                {
                    msg = String.format("lines:%d/%d\twords:%d\tcorrectWords:%d\tletters:%d\tcorrectLetters:%d\tWER:%.2f%%\tlineErrors:%d\twordErrors:%d", i, nofRefLines, nofWords, correctWords, nofLetters, correctLetters, 100.0 - (((float)correctWords * 100) / nofWords), errors, errors2);
                }
                //System.out.println(msg);
            }

            r = refLines.get(i);
            s = sysLines.get(i);
            
            fieldsRef = r.split("\\s+");
            fieldsSys = s.split("\\s+");
            
            if (fieldsRef.length != fieldsSys.length) // FIXME: fieldsRef.length
            {
                errors++;  
                msg2 = String.format("Error in number of words: Ref:%d Sys:%d, line:%d", fieldsRef.length, fieldsSys.length, i + 1);
                System.out.println (msg2);
                continue;
            }

            for (wordIndex = 0; wordIndex < fieldsSys.length; wordIndex++)
            {
                nofWords++;

                ref = fieldsRef[wordIndex].trim();
                sys = fieldsSys[wordIndex].trim();
                
                if (consonantClustering && wordIndex < fieldsSys.length - 1)
                {
                    // التقاء الساكنين in CA should be enhanced to cover more cases
                    if (sys.equals("عَنْ"))
                    {
                        if (fieldsRef[wordIndex + 1].startsWith("ا"))
                        {
                            sys = "عَنِ";
                        }
                    }
                    else if (sys.equals("مِنْ"))
                    {
                        if (fieldsRef[wordIndex + 1].startsWith("ا"))
                        {
                            sys = "مِنَ";
                        }
                    }
                }
                
                ref = removeDefaultDiac(ref);

                isMultiRefWord = false;
                gotMultiRefCorrect = false;
                curr_ref = ref;

                refDiacCodes = getDiacCodes(ref);
                refDiacCodesLen = refDiacCodes.length();
                
                sysDiacCodes = getDiacCodes(sys);
                sysDiacCodesLen = sysDiacCodes.length();
                
                if (ref.length() > 1 && refDiacCodesLen != sysDiacCodesLen){
                    all_refs = ref.split("/");
                }

                else {
                    all_refs = new String[] {ref};
                }

                if (all_refs.length > 1) {
                    nofMultiRefWords++;
                    isMultiRefWord = true;
                }

                for (String current_ref : all_refs) {
                    
                    curr_ref = current_ref;
                    sys = removeDefaultDiac(sys);
                    
                    msg = "";
                    
                    refDiacCodes = getDiacCodes(current_ref);
                    refDiacCodesLen = refDiacCodes.length();
                    
                    sysDiacCodes = getDiacCodes(sys);
                    sysDiacCodesLen = sysDiacCodes.length();
                    
                    diff = false;
                    nofLetters += refDiacCodesLen / 2;

                    if (refDiacCodesLen == sysDiacCodesLen)
                    {
                        for (j = 0; j < refDiacCodesLen / 2; j++)
                        {
                            if ((stemAccuracy == true) && (j == refDiacCodesLen / 2 - 1))
                            {
                                break;
                            }
                            if (refDiacCodes.charAt(j * 2) != sysDiacCodes.charAt(j * 2)){
                                errors3++;
                            }
                            
                            ch1 = refDiacCodes.charAt(j * 2);
                            d1 = refDiacCodes.charAt(j * 2 + 1);

                            ch2 = sysDiacCodes.charAt(j * 2);
                            d2 = sysDiacCodes.charAt(j * 2 + 1);
                            
                            nextCh1 = 'X';
                            nextCh2 = 'Y';
                            nextDiac1 = 'Z';
                            nextDiac2 = 'W';
                            if (j < refDiacCodesLen / 2 - 1)
                            {
                                nextCh1 = refDiacCodes.charAt((j + 1) * 2);
                                nextDiac1 = refDiacCodes.charAt((j + 1) * 2 + 1);

                                nextCh2 = sysDiacCodes.charAt((j + 1) * 2);
                                nextDiac2 = sysDiacCodes.charAt((j + 1) * 2 + 1);
                            }
                            
                            if (d1 != d2)
                            {
                                if (d1 == CH_DIAC_CODE_EMPTY)
                                {
                                    // No diac
                                    d1 = d2;
                                }
                                else if ((d1 == CH_DIAC_CODE_SHADDA) && ((d2 == CH_DIAC_CODE_SHADDA_FATHA) || (d2 == CH_DIAC_CODE_SHADDA_KASRA) || (d2 == CH_DIAC_CODE_SHADDA_DAMMA) ||
                                        (d2 == CH_DIAC_CODE_SHADDA_FATHATAN) || (d2 == CH_DIAC_CODE_SHADDA_KASRATAN) || (d2 == CH_DIAC_CODE_SHADDA_DAMMATAN) || (d2 == CH_DIAC_CODE_SHADDA_SUKUN)))
                                {
                                    // Shadda
                                    d1 = d2;
                                }
                            }
                            
                            if ((d1 != d2) && (d1 == CH_DIAC_CODE_FATHATAN) && (nextDiac1 == CH_DIAC_CODE_EMPTY) && (d2 == CH_DIAC_CODE_EMPTY) && (nextDiac2 == CH_DIAC_CODE_FATHATAN))
                            {
                                // اِلْتِماساً / ااِلْتِمَاسًا
                                d1 = d2;
                                
                                j++;
                                correctLetters++;
                            }

                            if (d1 == d2)
                            {
                                correctLetters++;
                            }
                            else
                            {
                                diff = true;
                                if (!showDiffOnly)
                                {
                                    msg += String.format(" %c", refDiacCodes.charAt(j * 2));
                                }
                            }
                        }
                        
                        if (!diff)
                        {
                            correctWords++;
                            System.out.println(current_ref);

                            if (isMultiRefWord)
                            {
                                gotMultiRefCorrect = true;
                            }
                            break;
                        }

                        else if (!isMultiRefWord) {
                            System.out.println("ERROR   Ref: " + current_ref + " Sys: " + sys); //+ ", Line: " + (i + 1) + ", Word: " + (wordIndex + 1));
                        }
                    }

                    

                    else
                    {
                        errors2++;
                        msg2 = String.format("Error in length of words: current_ref:%s Sys:%s, Line:%d, Word:%d", current_ref, sys, i + 1, wordIndex + 1);
                        System.out.println (msg2);
                        break;
                    }
                    
                    if (!showDiffOnly)
                    {
                        System.out.println (msg);
                    }
                }

                if (isMultiRefWord && !gotMultiRefCorrect)
                {
                    msg2 = String.format("multi-ref word error: current_ref:%s Sys:%s, Line:%d, Word:%d", curr_ref, sys, i + 1, wordIndex + 1);
                    System.out.println (msg2);
                }
            }
        }

        if (!stemAccuracy)
        {
            msg = String.format("lines:%d/%d\twords:%d\tcorrectWords:%d\tletters:%d\tcorrectLetters:%d\tWER:%.2f%%\tDER:%.2f%%\tlineErrors:%d\twordErrors:%d\tMultiRefWords:%d", i, nofRefLines, nofWords, correctWords, nofLetters, correctLetters, 100.0 - (((float)correctWords * 100) / nofWords), 100.0 - (((float)correctLetters * 100) / nofLetters), errors, errors2, nofMultiRefWords);
        }
        else
        {
            msg = String.format("lines:%d/%d\twords:%d\tcorrectWords:%d\tletters:%d\tcorrectLetters:%d\tWER:%.2f%%\tlineErrors:%d\twordErrors:%d\tMultiRefWords:%d", i, nofRefLines, nofWords, correctWords, nofLetters, correctLetters, 100.0 - (((float)correctWords * 100) / nofWords), errors, errors2, nofMultiRefWords);
        }
        System.out.println(msg);
    }    
}
