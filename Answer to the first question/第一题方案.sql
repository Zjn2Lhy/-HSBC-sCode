--使用的是mysql 8.0 开发 和调试

--请根据上面的表结构写出SQL语句。
--1).按月计算，谁是工资最高的经理?
SELECT
	e.employee_name,
	e.employee_id,
	t2.avgSalary AS maxSalary_month 
FROM
	employee e 
	JOIN (
	SELECT
		sum( salary )/ count( salary ) avgSalary,
		employee_id
	FROM
		( SELECT * FROM salary WHERE employee_id IN ( SELECT employee_id FROM employee WHERE manager_id IS NOT NULL AND manager_id != '' ) ) t 
	GROUP BY
		employee_id
	) t2 ON e.employee_id = t2.employee_id
ORDER BY
	avgSalary desc limit 1

--2)谁是年薪最高的非经理?
SELECT
	s.sumSalary,
	e.* 
FROM
	(
	SELECT
		sum( t.salary ) sumSalary,
		t.employee_id,
		SUBSTR( date, 1, 4 ) AS yearStr 
	FROM
		( SELECT * FROM salary WHERE employee_id IN ( SELECT employee_id FROM employee WHERE manager_id IS NULL OR manager_id = '' ) ) t 
	GROUP BY
		employee_id,
		SUBSTR( date, 1, 4 ) 
	ORDER BY
		sum( t.salary ) DESC 
		LIMIT 1 
	) s
	JOIN employee e ON s.employee_id = e.employee_id;
	
	
--以下是hive sql的答案,但未调试,
--月薪最高的经理
with a as (
select employee_id,employee_name
from employee
where manager_id is not null or manager_id <> 'null'
),
b as (select e.employee_id,e.month
from (select employee_id,to_char(trunc(date),'mm') as month,row_number() over(partition by employee_id order by salary desc) as top
from employee_salary ) e
where e.top = 1)
select b.month,a.employee_name
from a left join b
on a.employee_id = b.employee_id

--年薪最高的非经理
with a as (
select employee_id,employee_name
from employee
where manager_id is null or manager_id == 'null'),
b as (select to_char(trunc(date),'yyyy') as year,employee_id,sum(salary) as salary_sum
from employee_salary
where to_char(trunc(date),'yyyy') = '2022'
group by to_char(trunc(date),'yyyy'),employee_id)
select b.year,a.employee_name
from a left join b
on a.employee_id = b.employee_id	
	