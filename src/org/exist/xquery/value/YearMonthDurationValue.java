/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-03 Wolfgang M. Meier
 *  wolfgang@exist-db.org
 *  http://exist.sourceforge.net
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 *  $Id$
 */

package org.exist.xquery.value;

import java.text.Collator;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.exist.xquery.Constants;
import org.exist.xquery.XPathException;

/**
 * @author wolf
 */
public class YearMonthDurationValue extends DurationValue {

	public YearMonthDurationValue(int months) {
		if (months < 0) {
			negative = true;
			months *= -1;
		}
		this.year = months / 12;
		this.month = months % 12;
	}

	public YearMonthDurationValue(DurationValue other) {
		this.year = other.year;
		this.month = other.month;
		this.negative = other.negative;
	}

	public YearMonthDurationValue(String str) throws XPathException {
		super();
		// format is: [+|-]PnYnM
		if (str.length() < 3)
			throw new XPathException("Type error: xs:duration should start with [+|-]P");
		char ch;
		StringBuffer buf = new StringBuffer();
		int p = 0;
		int state = 0;
		int value = -1;
		while (p < str.length()) {
			ch = str.charAt(p);
			switch (ch) {
				case '-' :
					if (state > 0)
						throw new XPathException(
							"Type error in xs:yearMonthDuration: "
								+ str
								+ ": - is not allowed here");
					negative = true;
					break;
				case 'P' :
					if (state > 0)
						throw new XPathException(
							"Type error in xs:yearMonthDuration: "
								+ str
								+ ": P is not allowed here");
					state++;
					break;
				case 'Y' :
					if (state != 1 || value < 0)
						throw new XPathException(
							"Type error in xs:yearMonthDuration: "
								+ str
								+ ": Y is not allowed to occur here");
					year = value;
					value = -1;
					state++;
					break;
				case 'M' :
					if (state > 2 || value < 0)
						throw new XPathException(
							"Type error in xs:yearMonthDuration: "
								+ str
								+ ": M is not allowed to occur here");
					month = value;
					state++;
					value = -1;
					break;
				case '1' :
				case '2' :
				case '3' :
				case '4' :
				case '5' :
				case '6' :
				case '7' :
				case '8' :
				case '9' :
				case '0' :
					if (state < 1)
						throw new XPathException(
							"Type error in xs:yearMonthDuration: "
								+ str
								+ ": numeric value not allowed at this position");
					buf.append(ch);
					p++;
					while (p < str.length()) {
						ch = str.charAt(p);
						if (ch == '1'
							|| ch == '2'
							|| ch == '3'
							|| ch == '4'
							|| ch == '5'
							|| ch == '6'
							|| ch == '7'
							|| ch == '8'
							|| ch == '9'
							|| ch == '0')
							buf.append(ch);
						else {
							value = Integer.parseInt(buf.toString());
							buf.setLength(0);
							p--;
							break;
						}
						p++;
					}
					break;
			}
			p++;
		}
	}

	public int getValue() {
		int value = year * 12 + month;
		return negative ? -value : value;
	}

	/* (non-Javadoc)
	 * @see org.exist.xquery.value.DurationValue#getType()
	 */
	public int getType() {
		return Type.YEAR_MONTH_DURATION;
	}

	/* (non-Javadoc)
	 * @see org.exist.xquery.value.DurationValue#getStringValue()
	 */
	public String getStringValue() throws XPathException {
		StringBuffer buf = new StringBuffer();
		if (negative)
			buf.append('-');
		buf.append('P');
		if (year > 0)
			buf.append(year).append('Y');
		if (month > 0)
			buf.append(month).append('M');
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see org.exist.xquery.value.DurationValue#convertTo(int)
	 */
	public AtomicValue convertTo(int requiredType) throws XPathException {
		switch (requiredType) {
			case Type.ITEM :
			case Type.ATOMIC :
			case Type.YEAR_MONTH_DURATION :
				return this;
			case Type.STRING :
				return new StringValue(getStringValue());
			case Type.DURATION :
				return new DurationValue(this);
			default :
				throw new XPathException(
					"Type error: cannot cast xs:yearMonthDuration to "
						+ Type.getTypeName(requiredType));
		}
	}

	/* (non-Javadoc)
	 * @see org.exist.xquery.value.AtomicValue#compareTo(int, org.exist.xquery.value.AtomicValue)
	 */
	public boolean compareTo(Collator collator, int operator, AtomicValue other) throws XPathException {
		if (other.getType() == Type.YEAR_MONTH_DURATION) {
			int v1 = getValue();
			int v2 = ((YearMonthDurationValue) other).getValue();
			switch (operator) {
				case Constants.EQ :
					return v1 == v2;
				case Constants.NEQ :
					return v1 != v2;
				case Constants.LT :
					return v1 < v2;
				case Constants.LTEQ :
					return v1 <= v2;
				case Constants.GT :
					return v1 > v2;
				case Constants.GTEQ :
					return v1 >= v2;
				default :
					throw new XPathException("Unknown operator type in comparison");
			}
		} else
			throw new XPathException(
				"Type error: cannot compare xs:yearMonthDuration to "
					+ Type.getTypeName(other.getType()));
	}

	/* (non-Javadoc)
	 * @see org.exist.xquery.value.AtomicValue#compareTo(org.exist.xquery.value.AtomicValue)
	 */
	public int compareTo(Collator collator, AtomicValue other) throws XPathException {
		if (other.getType() == Type.YEAR_MONTH_DURATION) {
			int v1 = getValue();
			int v2 = ((YearMonthDurationValue) other).getValue();
			if (v1 == v2)
				return 0;
			else if (v1 < v2)
				return -1;
			else
				return 1;
		} else
			throw new XPathException(
				"Type error: cannot compare xs:yearMonthDuration to "
					+ Type.getTypeName(other.getType()));
	}

	/* (non-Javadoc)
	 * @see org.exist.xquery.value.AtomicValue#max(org.exist.xquery.value.AtomicValue)
	 */
	public AtomicValue max(Collator collator, AtomicValue other) throws XPathException {
		if (other.getType() == Type.YEAR_MONTH_DURATION)
			return compareTo(null, other) > 0 ? this : other;
		else
			return compareTo(null, other.convertTo(Type.YEAR_MONTH_DURATION)) > 0
				? this
				: other;
	}

	/* (non-Javadoc)
	 * @see org.exist.xquery.value.AtomicValue#max(org.exist.xquery.value.AtomicValue)
	 */
	public AtomicValue min(Collator collator, AtomicValue other) throws XPathException {
		if (other.getType() == Type.YEAR_MONTH_DURATION)
			return compareTo(null, other) < 0 ? this : other;
		else
			return compareTo(null, other.convertTo(Type.YEAR_MONTH_DURATION)) < 0
				? this
				: other;
	}

	/* (non-Javadoc)
	 * @see org.exist.xquery.value.DurationValue#plus(org.exist.xquery.value.ComputableValue)
	 */
	public ComputableValue plus(ComputableValue other) throws XPathException {
		switch (other.getType()) {
			case Type.YEAR_MONTH_DURATION :
				return new YearMonthDurationValue(getValue() + ((YearMonthDurationValue) other).getValue());
			case Type.DATE :
			case Type.DATE_TIME:
				int tzOffset = ((AbstractDateTimeValue) other).tzOffset;
				GregorianCalendar ncal = (GregorianCalendar) ((AbstractDateTimeValue) other).calendar.clone();
				ncal.add(Calendar.MONTH, getValue());
				if(other.getType() == Type.DATE) 
					return new DateValue(ncal, tzOffset);
				else
					new DateTimeValue(ncal, tzOffset);
			default:
				throw new XPathException(
					"Operand to plus should be of type xdt:yearMonthDuration, xs:date, "
						+ "or xs:dateTime; got: "
						+ Type.getTypeName(other.getType()));
		}
	}

	public ComputableValue minus(ComputableValue other) throws XPathException {
		switch (other.getType()) {
			case Type.YEAR_MONTH_DURATION :
				return new YearMonthDurationValue(
					getValue() - ((YearMonthDurationValue) other).getValue());
			default:
				throw new XPathException(
					"Operand to plus should be of type xdt:yearMonthDuration, xs:date, "
						+ "or xs:dateTime; got: "
						+ Type.getTypeName(other.getType()));
		}
	}
	
	public ComputableValue div(ComputableValue other) throws XPathException {
		if (other.getType() == Type.YEAR_MONTH_DURATION) {
			return new IntegerValue(getValue()).div(new IntegerValue(((YearMonthDurationValue) other).getValue()));
		} else if (Type.subTypeOf(other.getType(), Type.NUMBER)) {
			return new YearMonthDurationValue(((NumericValue) new IntegerValue(getValue()).div(other)).round().getInt());
		} else
			throw new XPathException(
					"Operand to div should be of xdt:yearMonthDuration or numeric type; got: "
						+ Type.getTypeName(other.getType()));
	}
	
	public ComputableValue mult(ComputableValue other) throws XPathException {
		if (Type.subTypeOf(other.getType(), Type.NUMBER)) {
			return new YearMonthDurationValue(((NumericValue) new IntegerValue(getValue()).mult(other)).round().getInt());
		} else
			throw new XPathException(
					"Operand to mult should be of numeric type; got: "
						+ Type.getTypeName(other.getType()));
	}
}
