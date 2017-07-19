/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ====================================================================
 */

package org.apache.poi.ss.usermodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFConditionalFormatting;
import org.apache.poi.hssf.usermodel.HSSFConditionalFormattingRule;
import org.apache.poi.ss.ITestDataProvider;
import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType;
import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;

/**
 * Base tests for Conditional Formatting, for both HSSF and XSSF
 */
public abstract class BaseTestConditionalFormatting {
    private final ITestDataProvider _testDataProvider;

    public BaseTestConditionalFormatting(ITestDataProvider testDataProvider){
        _testDataProvider = testDataProvider;
    }
    
    protected abstract void assertColour(String hexExpected, Color actual);

    @Test
    public void testBasic() throws IOException {
        Workbook wb = _testDataProvider.createWorkbook();
        Sheet sh = wb.createSheet();
        SheetConditionalFormatting sheetCF = sh.getSheetConditionalFormatting();

        assertEquals(0, sheetCF.getNumConditionalFormattings());
        try {
            assertNull(sheetCF.getConditionalFormattingAt(0));
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Specified CF index 0 is outside the allowable range"));
        }

        try {
            sheetCF.removeConditionalFormatting(0);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Specified CF index 0 is outside the allowable range"));
        }

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule("1");
        ConditionalFormattingRule rule2 = sheetCF.createConditionalFormattingRule("2");
        ConditionalFormattingRule rule3 = sheetCF.createConditionalFormattingRule("3");
        ConditionalFormattingRule rule4 = sheetCF.createConditionalFormattingRule("4");
        try {
            sheetCF.addConditionalFormatting(null, rule1);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("regions must not be null"));
        }
        try {
            sheetCF.addConditionalFormatting(
                    new CellRangeAddress[]{ CellRangeAddress.valueOf("A1:A3") },
                    (ConditionalFormattingRule)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("cfRules must not be null"));
        }

        try {
            sheetCF.addConditionalFormatting(
                    new CellRangeAddress[]{ CellRangeAddress.valueOf("A1:A3") },
                    new ConditionalFormattingRule[0]);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("cfRules must not be empty"));
        }

        try {
            sheetCF.addConditionalFormatting(
                    new CellRangeAddress[]{ CellRangeAddress.valueOf("A1:A3") },
                    new ConditionalFormattingRule[]{rule1, rule2, rule3, rule4});
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Number of rules must not exceed 3"));
        }
        wb.close();
    }

    /**
     * Test format conditions based on a boolean formula
     */
    @Test
    public void testBooleanFormulaConditions() throws IOException {
        Workbook wb = _testDataProvider.createWorkbook();
        Sheet sh = wb.createSheet();
        SheetConditionalFormatting sheetCF = sh.getSheetConditionalFormatting();

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule("SUM(A1:A5)>10");
        assertEquals(ConditionType.FORMULA, rule1.getConditionType());
        assertEquals("SUM(A1:A5)>10", rule1.getFormula1());
        int formatIndex1 = sheetCF.addConditionalFormatting(
                new CellRangeAddress[]{
                        CellRangeAddress.valueOf("B1"),
                        CellRangeAddress.valueOf("C3"),
                }, rule1);
        assertEquals(0, formatIndex1);
        assertEquals(1, sheetCF.getNumConditionalFormattings());
        CellRangeAddress[] ranges1 = sheetCF.getConditionalFormattingAt(formatIndex1).getFormattingRanges();
        assertEquals(2, ranges1.length);
        assertEquals("B1", ranges1[0].formatAsString());
        assertEquals("C3", ranges1[1].formatAsString());

        // adjacent address are merged
        int formatIndex2 = sheetCF.addConditionalFormatting(
                new CellRangeAddress[]{
                        CellRangeAddress.valueOf("B1"),
                        CellRangeAddress.valueOf("B2"),
                        CellRangeAddress.valueOf("B3"),
                }, rule1);
        assertEquals(1, formatIndex2);
        assertEquals(2, sheetCF.getNumConditionalFormattings());
        CellRangeAddress[] ranges2 = sheetCF.getConditionalFormattingAt(formatIndex2).getFormattingRanges();
        assertEquals(1, ranges2.length);
        assertEquals("B1:B3", ranges2[0].formatAsString());
        wb.close();
    }

    @Test
    public void testSingleFormulaConditions() throws IOException {
        Workbook wb = _testDataProvider.createWorkbook();
        Sheet sh = wb.createSheet();
        SheetConditionalFormatting sheetCF = sh.getSheetConditionalFormatting();

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.EQUAL, "SUM(A1:A5)+10");
        assertEquals(ConditionType.CELL_VALUE_IS, rule1.getConditionType());
        assertEquals("SUM(A1:A5)+10", rule1.getFormula1());
        assertEquals(ComparisonOperator.EQUAL, rule1.getComparisonOperation());

        ConditionalFormattingRule rule2 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.NOT_EQUAL, "15");
        assertEquals(ConditionType.CELL_VALUE_IS, rule2.getConditionType());
        assertEquals("15", rule2.getFormula1());
        assertEquals(ComparisonOperator.NOT_EQUAL, rule2.getComparisonOperation());

        ConditionalFormattingRule rule3 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.NOT_EQUAL, "15");
        assertEquals(ConditionType.CELL_VALUE_IS, rule3.getConditionType());
        assertEquals("15", rule3.getFormula1());
        assertEquals(ComparisonOperator.NOT_EQUAL, rule3.getComparisonOperation());

        ConditionalFormattingRule rule4 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.GT, "0");
        assertEquals(ConditionType.CELL_VALUE_IS, rule4.getConditionType());
        assertEquals("0", rule4.getFormula1());
        assertEquals(ComparisonOperator.GT, rule4.getComparisonOperation());

        ConditionalFormattingRule rule5 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.LT, "0");
        assertEquals(ConditionType.CELL_VALUE_IS, rule5.getConditionType());
        assertEquals("0", rule5.getFormula1());
        assertEquals(ComparisonOperator.LT, rule5.getComparisonOperation());

        ConditionalFormattingRule rule6 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.GE, "0");
        assertEquals(ConditionType.CELL_VALUE_IS, rule6.getConditionType());
        assertEquals("0", rule6.getFormula1());
        assertEquals(ComparisonOperator.GE, rule6.getComparisonOperation());

        ConditionalFormattingRule rule7 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.LE, "0");
        assertEquals(ConditionType.CELL_VALUE_IS, rule7.getConditionType());
        assertEquals("0", rule7.getFormula1());
        assertEquals(ComparisonOperator.LE, rule7.getComparisonOperation());

        ConditionalFormattingRule rule8 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.BETWEEN, "0", "5");
        assertEquals(ConditionType.CELL_VALUE_IS, rule8.getConditionType());
        assertEquals("0", rule8.getFormula1());
        assertEquals("5", rule8.getFormula2());
        assertEquals(ComparisonOperator.BETWEEN, rule8.getComparisonOperation());

        ConditionalFormattingRule rule9 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.NOT_BETWEEN, "0", "5");
        assertEquals(ConditionType.CELL_VALUE_IS, rule9.getConditionType());
        assertEquals("0", rule9.getFormula1());
        assertEquals("5", rule9.getFormula2());
        assertEquals(ComparisonOperator.NOT_BETWEEN, rule9.getComparisonOperation());
        
        wb.close();
    }

    @Test
    public void testCopy() throws IOException {
        Workbook wb = _testDataProvider.createWorkbook();
        Sheet sheet1 = wb.createSheet();
        Sheet sheet2 = wb.createSheet();
        SheetConditionalFormatting sheet1CF = sheet1.getSheetConditionalFormatting();
        SheetConditionalFormatting sheet2CF = sheet2.getSheetConditionalFormatting();
        assertEquals(0, sheet1CF.getNumConditionalFormattings());
        assertEquals(0, sheet2CF.getNumConditionalFormattings());

        ConditionalFormattingRule rule1 = sheet1CF.createConditionalFormattingRule(
                ComparisonOperator.EQUAL, "SUM(A1:A5)+10");

        ConditionalFormattingRule rule2 = sheet1CF.createConditionalFormattingRule(
                ComparisonOperator.NOT_EQUAL, "15");

        // adjacent address are merged
        int formatIndex = sheet1CF.addConditionalFormatting(
                new CellRangeAddress[]{
                        CellRangeAddress.valueOf("A1:A5"),
                        CellRangeAddress.valueOf("C1:C5")
                }, rule1, rule2);
        assertEquals(0, formatIndex);
        assertEquals(1, sheet1CF.getNumConditionalFormattings());

        assertEquals(0, sheet2CF.getNumConditionalFormattings());
        sheet2CF.addConditionalFormatting(sheet1CF.getConditionalFormattingAt(formatIndex));
        assertEquals(1, sheet2CF.getNumConditionalFormattings());

        ConditionalFormatting sheet2cf = sheet2CF.getConditionalFormattingAt(0);
        assertEquals(2, sheet2cf.getNumberOfRules());
        assertEquals("SUM(A1:A5)+10", sheet2cf.getRule(0).getFormula1());
        assertEquals(ComparisonOperator.EQUAL, sheet2cf.getRule(0).getComparisonOperation());
        assertEquals(ConditionType.CELL_VALUE_IS, sheet2cf.getRule(0).getConditionType());
        assertEquals("15", sheet2cf.getRule(1).getFormula1());
        assertEquals(ComparisonOperator.NOT_EQUAL, sheet2cf.getRule(1).getComparisonOperation());
        assertEquals(ConditionType.CELL_VALUE_IS, sheet2cf.getRule(1).getConditionType());
        
        wb.close();
    }

    @Test
    public void testRemove() throws IOException {
        Workbook wb = _testDataProvider.createWorkbook();
        Sheet sheet1 = wb.createSheet();
        SheetConditionalFormatting sheetCF = sheet1.getSheetConditionalFormatting();
        assertEquals(0, sheetCF.getNumConditionalFormattings());

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.EQUAL, "SUM(A1:A5)");

        // adjacent address are merged
        int formatIndex = sheetCF.addConditionalFormatting(
                new CellRangeAddress[]{
                        CellRangeAddress.valueOf("A1:A5")
                }, rule1);
        assertEquals(0, formatIndex);
        assertEquals(1, sheetCF.getNumConditionalFormattings());
        sheetCF.removeConditionalFormatting(0);
        assertEquals(0, sheetCF.getNumConditionalFormattings());
        try {
            assertNull(sheetCF.getConditionalFormattingAt(0));
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Specified CF index 0 is outside the allowable range"));
        }

        formatIndex = sheetCF.addConditionalFormatting(
                new CellRangeAddress[]{
                        CellRangeAddress.valueOf("A1:A5")
                }, rule1);
        assertEquals(0, formatIndex);
        assertEquals(1, sheetCF.getNumConditionalFormattings());
        sheetCF.removeConditionalFormatting(0);
        assertEquals(0, sheetCF.getNumConditionalFormattings());
        try {
            assertNull(sheetCF.getConditionalFormattingAt(0));
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Specified CF index 0 is outside the allowable range"));
        }
        
        wb.close();
    }
    
    @Test
    public void testCreateCF() throws IOException {
        Workbook workbook = _testDataProvider.createWorkbook();
        Sheet sheet = workbook.createSheet();
        String formula = "7";

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(formula);
        FontFormatting fontFmt = rule1.createFontFormatting();
        fontFmt.setFontStyle(true, false);

        BorderFormatting bordFmt = rule1.createBorderFormatting();
        bordFmt.setBorderBottom(BorderStyle.THIN);
        bordFmt.setBorderTop(BorderStyle.THICK);
        bordFmt.setBorderLeft(BorderStyle.DASHED);
        bordFmt.setBorderRight(BorderStyle.DOTTED);

        PatternFormatting patternFmt = rule1.createPatternFormatting();
        patternFmt.setFillBackgroundColor(IndexedColors.YELLOW.index);


        ConditionalFormattingRule rule2 = sheetCF.createConditionalFormattingRule(ComparisonOperator.BETWEEN, "1", "2");
        ConditionalFormattingRule [] cfRules =
        {
            rule1, rule2
        };

        short col = 1;
        CellRangeAddress [] regions = {
            new CellRangeAddress(0, 65535, col, col)
        };

        sheetCF.addConditionalFormatting(regions, cfRules);
        sheetCF.addConditionalFormatting(regions, cfRules);

        // Verification
        assertEquals(2, sheetCF.getNumConditionalFormattings());
        sheetCF.removeConditionalFormatting(1);
        assertEquals(1, sheetCF.getNumConditionalFormattings());
        ConditionalFormatting cf = sheetCF.getConditionalFormattingAt(0);
        assertNotNull(cf);

        regions = cf.getFormattingRanges();
        assertNotNull(regions);
        assertEquals(1, regions.length);
        CellRangeAddress r = regions[0];
        assertEquals(1, r.getFirstColumn());
        assertEquals(1, r.getLastColumn());
        assertEquals(0, r.getFirstRow());
        assertEquals(65535, r.getLastRow());

        assertEquals(2, cf.getNumberOfRules());

        rule1 = cf.getRule(0);
        assertEquals("7",rule1.getFormula1());
        assertNull(rule1.getFormula2());

        FontFormatting    r1fp = rule1.getFontFormatting();
        assertNotNull(r1fp);

        assertTrue(r1fp.isItalic());
        assertFalse(r1fp.isBold());

        BorderFormatting  r1bf = rule1.getBorderFormatting();
        assertNotNull(r1bf);
        assertEquals(BorderStyle.THIN, r1bf.getBorderBottomEnum());
        assertEquals(BorderStyle.THICK,r1bf.getBorderTopEnum());
        assertEquals(BorderStyle.DASHED,r1bf.getBorderLeftEnum());
        assertEquals(BorderStyle.DOTTED,r1bf.getBorderRightEnum());

        PatternFormatting r1pf = rule1.getPatternFormatting();
        assertNotNull(r1pf);
//        assertEquals(IndexedColors.YELLOW.index,r1pf.getFillBackgroundColor());

        rule2 = cf.getRule(1);
        assertEquals("2",rule2.getFormula2());
        assertEquals("1",rule2.getFormula1());
        
        workbook.close();
    }

    @Test
    public void testClone() throws IOException {

        Workbook wb = _testDataProvider.createWorkbook();
        Sheet sheet = wb.createSheet();
        String formula = "7";

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(formula);
        FontFormatting fontFmt = rule1.createFontFormatting();
        fontFmt.setFontStyle(true, false);

        PatternFormatting patternFmt = rule1.createPatternFormatting();
        patternFmt.setFillBackgroundColor(IndexedColors.YELLOW.index);


        ConditionalFormattingRule rule2 = sheetCF.createConditionalFormattingRule(ComparisonOperator.BETWEEN, "1", "2");
        ConditionalFormattingRule [] cfRules =
        {
            rule1, rule2
        };

        short col = 1;
        CellRangeAddress [] regions = {
            new CellRangeAddress(0, 65535, col, col)
        };

        sheetCF.addConditionalFormatting(regions, cfRules);

        try {
            wb.cloneSheet(0);
            assertEquals(2, wb.getNumberOfSheets());
        } catch (RuntimeException e) {
            if (e.getMessage().indexOf("needs to define a clone method") > 0) {
                fail("Indentified bug 45682");
            }
            throw e;
        } finally {
            wb.close();
        }
    }

    @Test
    public void testShiftRows() throws IOException {
        Workbook wb = _testDataProvider.createWorkbook();
        Sheet sheet = wb.createSheet();

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.BETWEEN, "SUM(A10:A15)", "1+SUM(B16:B30)");
        FontFormatting fontFmt = rule1.createFontFormatting();
        fontFmt.setFontStyle(true, false);

        PatternFormatting patternFmt = rule1.createPatternFormatting();
        patternFmt.setFillBackgroundColor(IndexedColors.YELLOW.index);

        ConditionalFormattingRule rule2 = sheetCF.createConditionalFormattingRule(
                ComparisonOperator.BETWEEN, "SUM(A10:A15)", "1+SUM(B16:B30)");
        BorderFormatting borderFmt = rule2.createBorderFormatting();
        borderFmt.setBorderDiagonal(BorderStyle.MEDIUM);

        CellRangeAddress [] regions = {
            new CellRangeAddress(2, 4, 0, 0), // A3:A5
        };
        sheetCF.addConditionalFormatting(regions, rule1);
        sheetCF.addConditionalFormatting(regions, rule2);

        // This row-shift should destroy the CF region
        sheet.shiftRows(10, 20, -9);
        assertEquals(0, sheetCF.getNumConditionalFormattings());

        // re-add the CF
        sheetCF.addConditionalFormatting(regions, rule1);
        sheetCF.addConditionalFormatting(regions, rule2);

        // This row shift should only affect the formulas
        sheet.shiftRows(14, 17, 8);
        ConditionalFormatting cf1 = sheetCF.getConditionalFormattingAt(0);
        assertEquals("SUM(A10:A23)", cf1.getRule(0).getFormula1());
        assertEquals("1+SUM(B24:B30)", cf1.getRule(0).getFormula2());
        ConditionalFormatting cf2 = sheetCF.getConditionalFormattingAt(1);
        assertEquals("SUM(A10:A23)", cf2.getRule(0).getFormula1());
        assertEquals("1+SUM(B24:B30)", cf2.getRule(0).getFormula2());

        sheet.shiftRows(0, 8, 21);
        cf1 = sheetCF.getConditionalFormattingAt(0);
        assertEquals("SUM(A10:A21)", cf1.getRule(0).getFormula1());
        assertEquals("1+SUM(#REF!)", cf1.getRule(0).getFormula2());
        cf2 = sheetCF.getConditionalFormattingAt(1);
        assertEquals("SUM(A10:A21)", cf2.getRule(0).getFormula1());
        assertEquals("1+SUM(#REF!)", cf2.getRule(0).getFormula2());

        wb.close();
    }

    protected void testRead(String filename) throws IOException {
        Workbook wb = _testDataProvider.openSampleWorkbook(filename);
        Sheet sh = wb.getSheet("CF");
        SheetConditionalFormatting sheetCF = sh.getSheetConditionalFormatting();
        assertEquals(3, sheetCF.getNumConditionalFormattings());

        ConditionalFormatting cf1 = sheetCF.getConditionalFormattingAt(0);
        assertEquals(2, cf1.getNumberOfRules());

        CellRangeAddress[] regions1 = cf1.getFormattingRanges();
        assertEquals(1, regions1.length);
        assertEquals("A1:A8", regions1[0].formatAsString());

        // CF1 has two rules: values less than -3 are bold-italic red, values greater than 3 are green
        ConditionalFormattingRule rule1 = cf1.getRule(0);
        assertEquals(ConditionType.CELL_VALUE_IS, rule1.getConditionType());
        assertEquals(ComparisonOperator.GT, rule1.getComparisonOperation());
        assertEquals("3", rule1.getFormula1());
        assertNull(rule1.getFormula2());
        // fills and borders are not set
        assertNull(rule1.getPatternFormatting());
        assertNull(rule1.getBorderFormatting());

        FontFormatting fmt1 = rule1.getFontFormatting();
//        assertEquals(IndexedColors.GREEN.index, fmt1.getFontColorIndex());
        assertTrue(fmt1.isBold());
        assertFalse(fmt1.isItalic());

        ConditionalFormattingRule rule2 = cf1.getRule(1);
        assertEquals(ConditionType.CELL_VALUE_IS, rule2.getConditionType());
        assertEquals(ComparisonOperator.LT, rule2.getComparisonOperation());
        assertEquals("-3", rule2.getFormula1());
        assertNull(rule2.getFormula2());
        assertNull(rule2.getPatternFormatting());
        assertNull(rule2.getBorderFormatting());

        FontFormatting fmt2 = rule2.getFontFormatting();
//        assertEquals(IndexedColors.RED.index, fmt2.getFontColorIndex());
        assertTrue(fmt2.isBold());
        assertTrue(fmt2.isItalic());


        ConditionalFormatting cf2 = sheetCF.getConditionalFormattingAt(1);
        assertEquals(1, cf2.getNumberOfRules());
        CellRangeAddress[] regions2 = cf2.getFormattingRanges();
        assertEquals(1, regions2.length);
        assertEquals("B9", regions2[0].formatAsString());

        ConditionalFormattingRule rule3 = cf2.getRule(0);
        assertEquals(ConditionType.FORMULA, rule3.getConditionType());
        assertEquals(ComparisonOperator.NO_COMPARISON, rule3.getComparisonOperation());
        assertEquals("$A$8>5", rule3.getFormula1());
        assertNull(rule3.getFormula2());

        FontFormatting fmt3 = rule3.getFontFormatting();
//        assertEquals(IndexedColors.RED.index, fmt3.getFontColorIndex());
        assertTrue(fmt3.isBold());
        assertTrue(fmt3.isItalic());

        PatternFormatting fmt4 = rule3.getPatternFormatting();
//        assertEquals(IndexedColors.LIGHT_CORNFLOWER_BLUE.index, fmt4.getFillBackgroundColor());
//        assertEquals(IndexedColors.AUTOMATIC.index, fmt4.getFillForegroundColor());
        assertEquals(PatternFormatting.NO_FILL, fmt4.getFillPattern());
        // borders are not set
        assertNull(rule3.getBorderFormatting());

        ConditionalFormatting cf3 = sheetCF.getConditionalFormattingAt(2);
        CellRangeAddress[] regions3 = cf3.getFormattingRanges();
        assertEquals(1, regions3.length);
        assertEquals("B1:B7", regions3[0].formatAsString());
        assertEquals(2, cf3.getNumberOfRules());

        ConditionalFormattingRule rule4 = cf3.getRule(0);
        assertEquals(ConditionType.CELL_VALUE_IS, rule4.getConditionType());
        assertEquals(ComparisonOperator.LE, rule4.getComparisonOperation());
        assertEquals("\"AAA\"", rule4.getFormula1());
        assertNull(rule4.getFormula2());

        ConditionalFormattingRule rule5 = cf3.getRule(1);
        assertEquals(ConditionType.CELL_VALUE_IS, rule5.getConditionType());
        assertEquals(ComparisonOperator.BETWEEN, rule5.getComparisonOperation());
        assertEquals("\"A\"", rule5.getFormula1());
        assertEquals("\"AAA\"", rule5.getFormula2());
        
        wb.close();
    }

    public void testReadOffice2007(String filename) throws IOException {
        Workbook wb = _testDataProvider.openSampleWorkbook(filename);
        Sheet s = wb.getSheet("CF");
        ConditionalFormatting cf = null;
        ConditionalFormattingRule cr = null;

        
        // Sanity check data
        assertEquals("Values", s.getRow(0).getCell(0).toString());
        assertEquals("10.0", s.getRow(2).getCell(0).toString());

        // Check we found all the conditional formatting rules we should have
        SheetConditionalFormatting sheetCF = s.getSheetConditionalFormatting();
        int numCF = 3;
        int numCF12 = 15;
        int numCFEX = 0; // TODO This should be 2, but we don't support CFEX formattings yet, see #58149
        assertEquals(numCF+numCF12+numCFEX, sheetCF.getNumConditionalFormattings());
        
        int fCF = 0, fCF12 = 0, fCFEX = 0;
        for (int i=0; i<sheetCF.getNumConditionalFormattings(); i++) {
            cf = sheetCF.getConditionalFormattingAt(i);
            if (cf instanceof HSSFConditionalFormatting) {
                String str = cf.toString();
                if (str.contains("[CF]")) fCF++;
                if (str.contains("[CF12]")) fCF12++;
                if (str.contains("[CFEX]")) fCFEX++;
            } else {
                ConditionType type = cf.getRule(cf.getNumberOfRules()-1).getConditionType();
                if (type == ConditionType.CELL_VALUE_IS ||
                    type == ConditionType.FORMULA) {
                    fCF++;
                } else {
                    // TODO Properly detect Ext ones from the xml
                    fCF12++;
                }
            }
        }
        assertEquals(numCF, fCF);
        assertEquals(numCF12, fCF12);
        assertEquals(numCFEX, fCFEX);
        
        
        // Check the rules / values in detail
        
        
        // Highlight Positive values - Column C
        cf = sheetCF.getConditionalFormattingAt(0);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("C2:C17", cf.getFormattingRanges()[0].formatAsString());
        
        assertEquals(1, cf.getNumberOfRules());
        cr = cf.getRule(0);
        assertEquals(ConditionType.CELL_VALUE_IS, cr.getConditionType());
        assertEquals(ComparisonOperator.GT, cr.getComparisonOperation());
        assertEquals("0", cr.getFormula1());
        assertEquals(null, cr.getFormula2());
        // When it matches:
        //   Sets the font colour to dark green
        //   Sets the background colour to lighter green
        // TODO Should the colours be slightly different between formats? Would CFEX support help for HSSF?
        if (cr instanceof HSSFConditionalFormattingRule) {
            assertColour("0:8080:0", cr.getFontFormatting().getFontColor());
            assertColour("CCCC:FFFF:CCCC", cr.getPatternFormatting().getFillBackgroundColorColor());
        } else {
            assertColour("006100", cr.getFontFormatting().getFontColor());
            assertColour("C6EFCE", cr.getPatternFormatting().getFillBackgroundColorColor());
        }
        
        
        // Highlight 10-30 - Column D
        cf = sheetCF.getConditionalFormattingAt(1);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("D2:D17", cf.getFormattingRanges()[0].formatAsString());
        
        assertEquals(1, cf.getNumberOfRules());
        cr = cf.getRule(0);
        assertEquals(ConditionType.CELL_VALUE_IS, cr.getConditionType());
        assertEquals(ComparisonOperator.BETWEEN, cr.getComparisonOperation());
        assertEquals("10", cr.getFormula1());
        assertEquals("30", cr.getFormula2());
        // When it matches:
        //   Sets the font colour to dark red
        //   Sets the background colour to lighter red
        // TODO Should the colours be slightly different between formats? Would CFEX support help for HSSF?
        if (cr instanceof HSSFConditionalFormattingRule) {
            assertColour("8080:0:8080", cr.getFontFormatting().getFontColor());
            assertColour("FFFF:9999:CCCC", cr.getPatternFormatting().getFillBackgroundColorColor());
        } else {
            assertColour("9C0006", cr.getFontFormatting().getFontColor());
            assertColour("FFC7CE", cr.getPatternFormatting().getFillBackgroundColorColor());
        }

        
        // Data Bars - Column E
        cf = sheetCF.getConditionalFormattingAt(2);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("E2:E17", cf.getFormattingRanges()[0].formatAsString());
        assertDataBar(cf, "FF63C384");
        
        
        // Colours Red->Yellow->Green - Column F
        cf = sheetCF.getConditionalFormattingAt(3);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("F2:F17", cf.getFormattingRanges()[0].formatAsString());
        assertColorScale(cf, "F8696B", "FFEB84", "63BE7B");

        
        // Colours Blue->White->Red - Column G
        cf = sheetCF.getConditionalFormattingAt(4);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("G2:G17", cf.getFormattingRanges()[0].formatAsString());
        assertColorScale(cf, "5A8AC6", "FCFCFF", "F8696B");

        
        // Icons : Default - Column H, percentage thresholds
        cf = sheetCF.getConditionalFormattingAt(5);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("H2:H17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GYR_3_TRAFFIC_LIGHTS, 0d, 33d, 67d);
        
        
        // Icons : 3 signs - Column I
        cf = sheetCF.getConditionalFormattingAt(6);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("I2:I17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GYR_3_SHAPES, 0d, 33d, 67d);
        
        
        // Icons : 3 traffic lights 2 - Column J
        cf = sheetCF.getConditionalFormattingAt(7);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("J2:J17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GYR_3_TRAFFIC_LIGHTS_BOX, 0d, 33d, 67d);
        
        
        // Icons : 4 traffic lights - Column K
        cf = sheetCF.getConditionalFormattingAt(8);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("K2:K17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GYRB_4_TRAFFIC_LIGHTS, 0d, 25d, 50d, 75d);

        
        // Icons : 3 symbols with backgrounds - Column L
        cf = sheetCF.getConditionalFormattingAt(9);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("L2:L17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GYR_3_SYMBOLS_CIRCLE, 0d, 33d, 67d);

        
        // Icons : 3 flags - Column M2 Only
        cf = sheetCF.getConditionalFormattingAt(10);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("M2", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GYR_3_FLAGS, 0d, 33d, 67d);

        // Icons : 3 flags - Column M (all)
        cf = sheetCF.getConditionalFormattingAt(11);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("M2:M17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GYR_3_FLAGS, 0d, 33d, 67d);

        
        // Icons : 3 symbols 2 (no background) - Column N
        cf = sheetCF.getConditionalFormattingAt(12);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("N2:N17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GYR_3_SYMBOLS, 0d, 33d, 67d);

        
        // Icons : 3 arrows - Column O
        cf = sheetCF.getConditionalFormattingAt(13);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("O2:O17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GYR_3_ARROW, 0d, 33d, 67d);

        
        // Icons : 5 arrows grey - Column P    
        cf = sheetCF.getConditionalFormattingAt(14);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("P2:P17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.GREY_5_ARROWS, 0d, 20d, 40d, 60d, 80d);

        
        // Icons : 3 stars (ext) - Column Q
        // TODO Support EXT formattings

        
        // Icons : 4 ratings - Column R
        cf = sheetCF.getConditionalFormattingAt(15);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("R2:R17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.RATINGS_4, 0d, 25d, 50d, 75d);

        
        // Icons : 5 ratings - Column S
        cf = sheetCF.getConditionalFormattingAt(16);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("S2:S17", cf.getFormattingRanges()[0].formatAsString());
        assertIconSetPercentages(cf, IconSet.RATINGS_5, 0d, 20d, 40d, 60d, 80d);

        
        // Custom Icon+Format - Column T
        cf = sheetCF.getConditionalFormattingAt(17);
        assertEquals(1, cf.getFormattingRanges().length);
        assertEquals("T2:T17", cf.getFormattingRanges()[0].formatAsString());
        
        // TODO Support IconSet + Other CFs with 2 rules
//        assertEquals(2, cf.getNumberOfRules());
//        cr = cf.getRule(0);
//        assertIconSetPercentages(cr, IconSet.GYR_3_TRAFFIC_LIGHTS_BOX, 0d, 33d, 67d);
//        cr = cf.getRule(1);
//        assertEquals(ConditionType.FORMULA, cr.getConditionType());
//        assertEquals(ComparisonOperator.NO_COMPARISON, cr.getComparisonOperation());
//        // TODO Why aren't these two the same between formats?
//        if (cr instanceof HSSFConditionalFormattingRule) {
//            assertEquals("MOD(ROW($T1),2)=1", cr.getFormula1());
//        } else {
//            assertEquals("MOD(ROW($T2),2)=1", cr.getFormula1());
//        }
//        assertEquals(null, cr.getFormula2());
        
        
        // Mixed icons - Column U
        // TODO Support EXT formattings
        
        wb.close();
    }
    
    private void assertDataBar(ConditionalFormatting cf, String color) {
        assertEquals(1, cf.getNumberOfRules());
        ConditionalFormattingRule cr = cf.getRule(0);
        assertDataBar(cr, color);
    }
    private void assertDataBar(ConditionalFormattingRule cr, String color) {
        assertEquals(ConditionType.DATA_BAR, cr.getConditionType());
        assertEquals(ComparisonOperator.NO_COMPARISON, cr.getComparisonOperation());
        assertEquals(null, cr.getFormula1());
        assertEquals(null, cr.getFormula2());
        
        DataBarFormatting databar = cr.getDataBarFormatting();
        assertNotNull(databar);
        assertEquals(false, databar.isIconOnly());
        assertEquals(true, databar.isLeftToRight());
        assertEquals(0, databar.getWidthMin());
        assertEquals(100, databar.getWidthMax());
        
        assertColour(color, databar.getColor());
        
        ConditionalFormattingThreshold th;
        th = databar.getMinThreshold();
        assertEquals(RangeType.MIN, th.getRangeType());
        assertEquals(null, th.getValue());
        assertEquals(null, th.getFormula());
        th = databar.getMaxThreshold();
        assertEquals(RangeType.MAX, th.getRangeType());
        assertEquals(null, th.getValue());
        assertEquals(null, th.getFormula());
    }
    
    private void assertIconSetPercentages(ConditionalFormatting cf, IconSet iconset, Double...vals) {
        assertEquals(1, cf.getNumberOfRules());
        ConditionalFormattingRule cr = cf.getRule(0);
        assertIconSetPercentages(cr, iconset, vals);
    }        
    private void assertIconSetPercentages(ConditionalFormattingRule cr, IconSet iconset, Double...vals) {
        assertEquals(ConditionType.ICON_SET, cr.getConditionType());
        assertEquals(ComparisonOperator.NO_COMPARISON, cr.getComparisonOperation());
        assertEquals(null, cr.getFormula1());
        assertEquals(null, cr.getFormula2());
        
        IconMultiStateFormatting icon = cr.getMultiStateFormatting();
        assertNotNull(icon);
        assertEquals(iconset, icon.getIconSet());
        assertEquals(false, icon.isIconOnly());
        assertEquals(false, icon.isReversed());
        
        assertNotNull(icon.getThresholds());
        assertEquals(vals.length, icon.getThresholds().length);
        for (int i=0; i<vals.length; i++) {
            Double v = vals[i];
            ConditionalFormattingThreshold th = icon.getThresholds()[i];
            assertEquals(RangeType.PERCENT, th.getRangeType());
            assertEquals(v, th.getValue());
            assertEquals(null, th.getFormula());
        }
    }
    
    private void assertColorScale(ConditionalFormatting cf, String... colors) {
        assertEquals(1, cf.getNumberOfRules());
        ConditionalFormattingRule cr = cf.getRule(0);
        assertColorScale(cr, colors);
    }        
    private void assertColorScale(ConditionalFormattingRule cr, String... colors) {
        assertEquals(ConditionType.COLOR_SCALE, cr.getConditionType());
        assertEquals(ComparisonOperator.NO_COMPARISON, cr.getComparisonOperation());
        assertEquals(null, cr.getFormula1());
        assertEquals(null, cr.getFormula2());
        
        ColorScaleFormatting color = cr.getColorScaleFormatting();
        assertNotNull(color);
        assertNotNull(color.getColors());
        assertNotNull(color.getThresholds());
        assertEquals(colors.length, color.getNumControlPoints());
        assertEquals(colors.length, color.getColors().length);
        assertEquals(colors.length, color.getThresholds().length);
        
        // Thresholds should be Min / (evenly spaced) / Max
        int steps = 100 / (colors.length-1);
        for (int i=0; i<colors.length; i++) {
            ConditionalFormattingThreshold th = color.getThresholds()[i];
            if (i == 0) {
                assertEquals(RangeType.MIN, th.getRangeType());
            } else if (i == colors.length-1) {
                assertEquals(RangeType.MAX, th.getRangeType());
            } else {
                assertEquals(RangeType.PERCENTILE, th.getRangeType());
                assertEquals(steps*i, th.getValue().intValue());
            }
            assertEquals(null, th.getFormula());
        }
        
        // Colors should match
        for (int i=0; i<colors.length; i++) {
            assertColour(colors[i], color.getColors()[i]);
        }
    }

    @Test
    public void testCreateFontFormatting() throws IOException {
        Workbook workbook = _testDataProvider.createWorkbook();
        Sheet sheet = workbook.createSheet();

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "7");
        FontFormatting fontFmt = rule1.createFontFormatting();
        assertFalse(fontFmt.isItalic());
        assertFalse(fontFmt.isBold());
        fontFmt.setFontStyle(true, true);
        assertTrue(fontFmt.isItalic());
        assertTrue(fontFmt.isBold());

        assertEquals(-1, fontFmt.getFontHeight()); // not modified
        fontFmt.setFontHeight(200);
        assertEquals(200, fontFmt.getFontHeight()); 
        fontFmt.setFontHeight(100);
        assertEquals(100, fontFmt.getFontHeight());

        assertEquals(FontFormatting.SS_NONE, fontFmt.getEscapementType());
        fontFmt.setEscapementType(FontFormatting.SS_SUB);
        assertEquals(FontFormatting.SS_SUB, fontFmt.getEscapementType());
        fontFmt.setEscapementType(FontFormatting.SS_NONE);
        assertEquals(FontFormatting.SS_NONE, fontFmt.getEscapementType());
        fontFmt.setEscapementType(FontFormatting.SS_SUPER);
        assertEquals(FontFormatting.SS_SUPER, fontFmt.getEscapementType());

        assertEquals(FontFormatting.U_NONE, fontFmt.getUnderlineType());
        fontFmt.setUnderlineType(FontFormatting.U_SINGLE);
        assertEquals(FontFormatting.U_SINGLE, fontFmt.getUnderlineType());
        fontFmt.setUnderlineType(FontFormatting.U_NONE);
        assertEquals(FontFormatting.U_NONE, fontFmt.getUnderlineType());
        fontFmt.setUnderlineType(FontFormatting.U_DOUBLE);
        assertEquals(FontFormatting.U_DOUBLE, fontFmt.getUnderlineType());

        assertEquals(-1, fontFmt.getFontColorIndex());
        fontFmt.setFontColorIndex(IndexedColors.RED.index);
        assertEquals(IndexedColors.RED.index, fontFmt.getFontColorIndex());
        fontFmt.setFontColorIndex(IndexedColors.AUTOMATIC.index);
        assertEquals(IndexedColors.AUTOMATIC.index, fontFmt.getFontColorIndex());
        fontFmt.setFontColorIndex(IndexedColors.BLUE.index);
        assertEquals(IndexedColors.BLUE.index, fontFmt.getFontColorIndex());

        ConditionalFormattingRule [] cfRules = { rule1 };

        CellRangeAddress [] regions = { CellRangeAddress.valueOf("A1:A5") };

        sheetCF.addConditionalFormatting(regions, cfRules);

        // Verification
        ConditionalFormatting cf = sheetCF.getConditionalFormattingAt(0);
        assertNotNull(cf);

        assertEquals(1, cf.getNumberOfRules());

        FontFormatting  r1fp = cf.getRule(0).getFontFormatting();
        assertNotNull(r1fp);

        assertTrue(r1fp.isItalic());
        assertTrue(r1fp.isBold());
        assertEquals(FontFormatting.SS_SUPER, r1fp.getEscapementType());
        assertEquals(FontFormatting.U_DOUBLE, r1fp.getUnderlineType());
        assertEquals(IndexedColors.BLUE.index, r1fp.getFontColorIndex());

        workbook.close();
    }

    @Test
    public void testCreatePatternFormatting() throws IOException {
        Workbook workbook = _testDataProvider.createWorkbook();
        Sheet sheet = workbook.createSheet();

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "7");
        PatternFormatting patternFmt = rule1.createPatternFormatting();

        assertEquals(0, patternFmt.getFillBackgroundColor());
        patternFmt.setFillBackgroundColor(IndexedColors.RED.index);
        assertEquals(IndexedColors.RED.index, patternFmt.getFillBackgroundColor());

        assertEquals(0, patternFmt.getFillForegroundColor());
        patternFmt.setFillForegroundColor(IndexedColors.BLUE.index);
        assertEquals(IndexedColors.BLUE.index, patternFmt.getFillForegroundColor());

        assertEquals(PatternFormatting.NO_FILL, patternFmt.getFillPattern());
        patternFmt.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        assertEquals(PatternFormatting.SOLID_FOREGROUND, patternFmt.getFillPattern());
        patternFmt.setFillPattern(PatternFormatting.NO_FILL);
        assertEquals(PatternFormatting.NO_FILL, patternFmt.getFillPattern());
        patternFmt.setFillPattern(PatternFormatting.BRICKS);
        assertEquals(PatternFormatting.BRICKS, patternFmt.getFillPattern());

        ConditionalFormattingRule [] cfRules = { rule1 };

        CellRangeAddress [] regions = { CellRangeAddress.valueOf("A1:A5") };

        sheetCF.addConditionalFormatting(regions, cfRules);

        // Verification
        ConditionalFormatting cf = sheetCF.getConditionalFormattingAt(0);
        assertNotNull(cf);

        assertEquals(1, cf.getNumberOfRules());

        PatternFormatting  r1fp = cf.getRule(0).getPatternFormatting();
        assertNotNull(r1fp);

        assertEquals(IndexedColors.RED.index, r1fp.getFillBackgroundColor());
        assertEquals(IndexedColors.BLUE.index, r1fp.getFillForegroundColor());
        assertEquals(PatternFormatting.BRICKS, r1fp.getFillPattern());
        
        workbook.close();
    }
    
    @Test
    public void testAllCreateBorderFormatting() throws IOException {
        // Make sure it is possible to create a conditional formatting rule
        // with every type of Border Style
        Workbook workbook = _testDataProvider.createWorkbook();
        Sheet sheet = workbook.createSheet();

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "7");
        BorderFormatting borderFmt = rule1.createBorderFormatting();

        for (BorderStyle border : BorderStyle.values()) {
            borderFmt.setBorderTop(border);
            assertEquals(border, borderFmt.getBorderTopEnum());

            borderFmt.setBorderBottom(border);
            assertEquals(border, borderFmt.getBorderBottomEnum());

            borderFmt.setBorderLeft(border);
            assertEquals(border, borderFmt.getBorderLeftEnum());

            borderFmt.setBorderRight(border);
            assertEquals(border, borderFmt.getBorderRightEnum());

            borderFmt.setBorderDiagonal(border);
            assertEquals(border, borderFmt.getBorderDiagonalEnum());
        }

        workbook.close();
    }

    @Test
    public void testCreateBorderFormatting() throws IOException {
        Workbook workbook = _testDataProvider.createWorkbook();
        Sheet sheet = workbook.createSheet();

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "7");
        BorderFormatting borderFmt = rule1.createBorderFormatting();

        assertEquals(BorderStyle.NONE, borderFmt.getBorderBottomEnum());
        borderFmt.setBorderBottom(BorderStyle.DOTTED);
        assertEquals(BorderStyle.DOTTED, borderFmt.getBorderBottomEnum());
        borderFmt.setBorderBottom(BorderStyle.NONE);
        assertEquals(BorderStyle.NONE, borderFmt.getBorderBottomEnum());
        borderFmt.setBorderBottom(BorderStyle.THICK);
        assertEquals(BorderStyle.THICK, borderFmt.getBorderBottomEnum());

        assertEquals(BorderStyle.NONE, borderFmt.getBorderTopEnum());
        borderFmt.setBorderTop(BorderStyle.DOTTED);
        assertEquals(BorderStyle.DOTTED, borderFmt.getBorderTopEnum());
        borderFmt.setBorderTop(BorderStyle.NONE);
        assertEquals(BorderStyle.NONE, borderFmt.getBorderTopEnum());
        borderFmt.setBorderTop(BorderStyle.THICK);
        assertEquals(BorderStyle.THICK, borderFmt.getBorderTopEnum());

        assertEquals(BorderStyle.NONE, borderFmt.getBorderLeftEnum());
        borderFmt.setBorderLeft(BorderStyle.DOTTED);
        assertEquals(BorderStyle.DOTTED, borderFmt.getBorderLeftEnum());
        borderFmt.setBorderLeft(BorderStyle.NONE);
        assertEquals(BorderStyle.NONE, borderFmt.getBorderLeftEnum());
        borderFmt.setBorderLeft(BorderStyle.THIN);
        assertEquals(BorderStyle.THIN, borderFmt.getBorderLeftEnum());

        assertEquals(BorderStyle.NONE, borderFmt.getBorderRightEnum());
        borderFmt.setBorderRight(BorderStyle.DOTTED);
        assertEquals(BorderStyle.DOTTED, borderFmt.getBorderRightEnum());
        borderFmt.setBorderRight(BorderStyle.NONE);
        assertEquals(BorderStyle.NONE, borderFmt.getBorderRightEnum());
        borderFmt.setBorderRight(BorderStyle.HAIR);
        assertEquals(BorderStyle.HAIR, borderFmt.getBorderRightEnum());

        ConditionalFormattingRule [] cfRules = { rule1 };

        CellRangeAddress [] regions = { CellRangeAddress.valueOf("A1:A5") };

        sheetCF.addConditionalFormatting(regions, cfRules);

        // Verification
        ConditionalFormatting cf = sheetCF.getConditionalFormattingAt(0);
        assertNotNull(cf);

        assertEquals(1, cf.getNumberOfRules());

        BorderFormatting  r1fp = cf.getRule(0).getBorderFormatting();
        assertNotNull(r1fp);
        assertEquals(BorderStyle.THICK, r1fp.getBorderBottomEnum());
        assertEquals(BorderStyle.THICK, r1fp.getBorderTopEnum());
        assertEquals(BorderStyle.THIN, r1fp.getBorderLeftEnum());
        assertEquals(BorderStyle.HAIR, r1fp.getBorderRightEnum());
        
        workbook.close();
    }
    
    @Test
    public void testCreateIconFormatting() throws IOException {
        Workbook wb1 = _testDataProvider.createWorkbook();
        Sheet sheet = wb1.createSheet();

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule1 = 
                sheetCF.createConditionalFormattingRule(IconSet.GYRB_4_TRAFFIC_LIGHTS);
        IconMultiStateFormatting iconFmt = rule1.getMultiStateFormatting();
        
        assertEquals(IconSet.GYRB_4_TRAFFIC_LIGHTS, iconFmt.getIconSet());
        assertEquals(4, iconFmt.getThresholds().length);
        assertEquals(false, iconFmt.isIconOnly());
        assertEquals(false, iconFmt.isReversed());
        
        iconFmt.setIconOnly(true);
        iconFmt.getThresholds()[0].setRangeType(RangeType.MIN);
        iconFmt.getThresholds()[1].setRangeType(RangeType.NUMBER);
        iconFmt.getThresholds()[1].setValue(10d);
        iconFmt.getThresholds()[2].setRangeType(RangeType.PERCENT);
        iconFmt.getThresholds()[2].setValue(75d);
        iconFmt.getThresholds()[3].setRangeType(RangeType.MAX);
        
        CellRangeAddress [] regions = { CellRangeAddress.valueOf("A1:A5") };
        sheetCF.addConditionalFormatting(regions, rule1);

        // Save, re-load and re-check
        Workbook wb2 = _testDataProvider.writeOutAndReadBack(wb1);
        wb1.close();
        sheet = wb2.getSheetAt(0);
        sheetCF = sheet.getSheetConditionalFormatting();
        assertEquals(1, sheetCF.getNumConditionalFormattings());
        
        ConditionalFormatting cf = sheetCF.getConditionalFormattingAt(0);
        assertEquals(1, cf.getNumberOfRules());
        rule1 = cf.getRule(0);
        assertEquals(ConditionType.ICON_SET, rule1.getConditionType());
        iconFmt = rule1.getMultiStateFormatting();
        
        assertEquals(IconSet.GYRB_4_TRAFFIC_LIGHTS, iconFmt.getIconSet());
        assertEquals(4, iconFmt.getThresholds().length);
        assertEquals(true, iconFmt.isIconOnly());
        assertEquals(false, iconFmt.isReversed());

        assertEquals(RangeType.MIN,    iconFmt.getThresholds()[0].getRangeType());
        assertEquals(RangeType.NUMBER, iconFmt.getThresholds()[1].getRangeType());
        assertEquals(RangeType.PERCENT,iconFmt.getThresholds()[2].getRangeType());
        assertEquals(RangeType.MAX,    iconFmt.getThresholds()[3].getRangeType());
        assertEquals(null, iconFmt.getThresholds()[0].getValue());
        assertEquals(10d,  iconFmt.getThresholds()[1].getValue(), 0);
        assertEquals(75d,  iconFmt.getThresholds()[2].getValue(), 0);
        assertEquals(null, iconFmt.getThresholds()[3].getValue());
        
        wb2.close();
    }
    
    @Test
    public void testCreateColorScaleFormatting() throws IOException {
        Workbook wb1 = _testDataProvider.createWorkbook();
        Sheet sheet = wb1.createSheet();

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule1 = 
                sheetCF.createConditionalFormattingColorScaleRule();
        ColorScaleFormatting clrFmt = rule1.getColorScaleFormatting();
        
        assertEquals(3, clrFmt.getNumControlPoints());
        assertEquals(3, clrFmt.getColors().length);
        assertEquals(3, clrFmt.getThresholds().length);
        
        clrFmt.getThresholds()[0].setRangeType(RangeType.MIN);
        clrFmt.getThresholds()[1].setRangeType(RangeType.NUMBER);
        clrFmt.getThresholds()[1].setValue(10d);
        clrFmt.getThresholds()[2].setRangeType(RangeType.MAX);
        
        CellRangeAddress [] regions = { CellRangeAddress.valueOf("A1:A5") };
        sheetCF.addConditionalFormatting(regions, rule1);
        
        // Save, re-load and re-check
        Workbook wb2 = _testDataProvider.writeOutAndReadBack(wb1);
        wb1.close();
        sheet = wb2.getSheetAt(0);
        sheetCF = sheet.getSheetConditionalFormatting();
        assertEquals(1, sheetCF.getNumConditionalFormattings());
        
        ConditionalFormatting cf = sheetCF.getConditionalFormattingAt(0);
        assertEquals(1, cf.getNumberOfRules());
        rule1 = cf.getRule(0);
        clrFmt = rule1.getColorScaleFormatting();
        assertEquals(ConditionType.COLOR_SCALE, rule1.getConditionType());
        
        assertEquals(3, clrFmt.getNumControlPoints());
        assertEquals(3, clrFmt.getColors().length);
        assertEquals(3, clrFmt.getThresholds().length);

        assertEquals(RangeType.MIN,    clrFmt.getThresholds()[0].getRangeType());
        assertEquals(RangeType.NUMBER, clrFmt.getThresholds()[1].getRangeType());
        assertEquals(RangeType.MAX,    clrFmt.getThresholds()[2].getRangeType());
        assertEquals(null, clrFmt.getThresholds()[0].getValue());
        assertEquals(10d,  clrFmt.getThresholds()[1].getValue(), 0);
        assertEquals(null, clrFmt.getThresholds()[2].getValue());
        
        wb2.close();
    }
    
    @Test
    public void testCreateDataBarFormatting() throws IOException {
        Workbook wb1 = _testDataProvider.createWorkbook();
        Sheet sheet = wb1.createSheet();

        String colorHex = "FFFFEB84";
        ExtendedColor color = wb1.getCreationHelper().createExtendedColor();
        color.setARGBHex(colorHex);
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule1 = 
                sheetCF.createConditionalFormattingRule(color);
        DataBarFormatting dbFmt = rule1.getDataBarFormatting();
        
        assertEquals(false, dbFmt.isIconOnly());
        assertEquals(true, dbFmt.isLeftToRight());
        assertEquals(0,   dbFmt.getWidthMin());
        assertEquals(100, dbFmt.getWidthMax());
        assertColour(colorHex, dbFmt.getColor());
        
        dbFmt.getMinThreshold().setRangeType(RangeType.MIN);
        dbFmt.getMaxThreshold().setRangeType(RangeType.MAX);
        
        CellRangeAddress [] regions = { CellRangeAddress.valueOf("A1:A5") };
        sheetCF.addConditionalFormatting(regions, rule1);
        
        // Save, re-load and re-check
        Workbook wb2 = _testDataProvider.writeOutAndReadBack(wb1);
        wb1.close();
        sheet = wb2.getSheetAt(0);
        sheetCF = sheet.getSheetConditionalFormatting();
        assertEquals(1, sheetCF.getNumConditionalFormattings());
        
        ConditionalFormatting cf = sheetCF.getConditionalFormattingAt(0);
        assertEquals(1, cf.getNumberOfRules());
        rule1 = cf.getRule(0);
        dbFmt = rule1.getDataBarFormatting();
        assertEquals(ConditionType.DATA_BAR, rule1.getConditionType());
        
        assertEquals(false, dbFmt.isIconOnly());
        assertEquals(true, dbFmt.isLeftToRight());
        assertEquals(0,   dbFmt.getWidthMin());
        assertEquals(100, dbFmt.getWidthMax());
        assertColour(colorHex, dbFmt.getColor());

        assertEquals(RangeType.MIN, dbFmt.getMinThreshold().getRangeType());
        assertEquals(RangeType.MAX, dbFmt.getMaxThreshold().getRangeType());
        assertEquals(null, dbFmt.getMinThreshold().getValue());
        assertEquals(null, dbFmt.getMaxThreshold().getValue());
        
        wb2.close();
    }
    
    @Test
    public void testBug55380() throws IOException {
        Workbook wb = _testDataProvider.createWorkbook();
        Sheet sheet = wb.createSheet();
        CellRangeAddress[] ranges = new CellRangeAddress[] {
            CellRangeAddress.valueOf("C9:D30"), CellRangeAddress.valueOf("C7:C31")
        };
        ConditionalFormattingRule rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule("$A$1>0");
        sheet.getSheetConditionalFormatting().addConditionalFormatting(ranges, rule);
        wb.close();
    }
}