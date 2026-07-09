package site.secmega.secapi.feature.outputDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.OutputDetail;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OutputDetailRepository extends JpaRepository<OutputDetail, Long>, JpaSpecificationExecutor<OutputDetail> {
    @Query("SELECT COALESCE(SUM(o.goodQty), 0) from OutputDetail o where o.deletedAt is null")
    long sumGoodOutputQty();

    @Query("SELECT COALESCE(SUM(o.goodQty), 0) FROM OutputDetail o WHERE o.workOrder.id = ?1")
    Integer sumGoodQtyByWorkOrder_Id(Long id);

    @Query("select COALESCE(SUM(o.goodQty), 0) from OutputDetail o where o.fromLine.id = ?1")
    Integer sumByFromLine_Id(Long id);

    @Query("select COALESCE(SUM(o.goodQty), 0) from OutputDetail o where o.outputDate = ?1 and o.fromLine.department.processNo = ?2")
    Integer totalInputByDate(LocalDate outputDate, Integer processNo);

    @Query("select COALESCE(SUM(o.goodQty), 0) from OutputDetail o where o.outputDate = ?1 and o.fromLine.department.processNo = ?2")
    Integer totalOutputSewingByDate(LocalDate outputDate, Integer processNo);

    @Query("""
            select COALESCE(SUM(o.goodQty), 0) from OutputDetail o
            where o.deletedAt is null and o.workOrder.mo = ?1 and o.outputDate = ?2 and o.fromLine.department.processNo = ?3""")
    Integer totalOutputTodayByMO(String mo, LocalDate outputDate, Integer processNo);

    @Query("""
            select COALESCE(SUM(o.goodQty), 0) from OutputDetail o
            where o.deletedAt is null and o.workOrder.mo = ?1 and o.outputDate = ?2 and o.fromLine.id = ?3""")
    Integer totalOutputTodayByMOAndLineId(String mo, LocalDate outputDate, Long id);

    @Query("select COALESCE(SUM(o.goodQty), 0) from OutputDetail o where o.deletedAt is null and o.outputDate = ?1 and o.time.id = ?2")
    Integer totalOutputByTimeId(LocalDate outputDate, Long id);


    @Query("""
            select COALESCE(SUM(o.goodQty), 0) from OutputDetail o
            where o.outputDate = ?1 and o.workOrder.mo = ?2 and o.fromLine.department.processNo = ?3 and o.size.id = ?4""")
    Integer totalOutputTodayByMOAndSize(LocalDate outputDate, String mo, Integer processNo, Long id);

    @Query("select COALESCE(SUM(o.goodQty), 0) from OutputDetail o where o.fromLine.department.processNo = ?1")
    Integer totalOutputQty(Integer processNo);

    @Query("select COALESCE(SUM(o.goodQty), 0) from OutputDetail o where o.outputDate = ?1 and o.fromLine.id = ?2")
    Integer sumOutputByLine(LocalDate outputDate, Long id);

    @Query("select COALESCE(SUM(o.goodQty), 0) from OutputDetail o where o.workOrder.mo = ?1 and o.outputDate = ?2 and o.fromLine.id = ?3")
    Integer sumOutputTodayByMOAndLine(String mo, LocalDate outputDate, Long id);

    @Query("""
            select COALESCE(SUM(o.goodQty), 0) from OutputDetail o
            where o.outputDate between ?1 and ?2 and o.fromLine.department.processNo = ?3 and o.deletedAt is null""")
    Integer totalInputBetweenDates(LocalDate outputDateStart, LocalDate outputDateEnd, Integer processNo);

    @Query("""
            select COALESCE(SUM(o.goodQty), 0) from OutputDetail o
            where o.outputDate between ?1 and ?2 and o.fromLine.department.processNo = ?3 and o.deletedAt is null""")
    Integer totalOutputSewingBetweenDates(LocalDate outputDateStart, LocalDate outputDateEnd, Integer processNo);

    @Query("""
            select COALESCE(SUM(o.goodQty), 0) from OutputDetail o
            where o.outputDate between ?1 and ?2 and o.fromLine.department.processNo = ?3 and o.time.id = ?4""")
    Integer totalOutputSewingBetweenDatesByTime(LocalDate outputDateStart, LocalDate outputDateEnd, Integer processNo, Long id);

    @Query("""
        SELECT 
            o.outputDate as date,
            COALESCE(SUM(CASE WHEN d.processNo = 1 THEN o.goodQty ELSE 0 END), 0) as input,
            COALESCE(SUM(CASE WHEN d.processNo = 2 THEN o.goodQty ELSE 0 END), 0) as output
        FROM OutputDetail o
        JOIN o.fromLine fl
        JOIN fl.department d
        WHERE o.deletedAt IS NULL
            AND o.outputDate BETWEEN :dateFrom AND :dateTo
        GROUP BY o.outputDate
        ORDER BY o.outputDate ASC
    """)
    List<Object[]> getDailySummaryBetweenDates(@Param("dateFrom") LocalDate dateFrom,
                                               @Param("dateTo") LocalDate dateTo);

    @Query("""
        SELECT COALESCE(SUM(od.goodQty), 0)
        FROM OutputDetail od
        WHERE od.outputDate BETWEEN :startDate AND :endDate
          AND od.fromLine.department.processNo = :processNo
          AND od.fromLine.id = :lineId
          AND od.time.id = :timeId
          AND od.deletedAt IS NULL
    """)
        Integer totalOutputSewingBetweenDatesByTimeAndLine(
                LocalDate startDate,
                LocalDate endDate,
                Integer processNo,
                Long timeId,
                Long lineId
        );

    @Query(value = """
        WITH hour_buckets AS (
            SELECT
                h.hour_ago,
                date_trunc('hour', CURRENT_TIMESTAMP AT TIME ZONE 'Asia/Bangkok') - (h.hour_ago * INTERVAL '1 hour') AS bucket_start
            FROM generate_series(0, 47) AS h(hour_ago)
        ),
        output_by_time AS (
            SELECT
                od.good_qty,
                od.output_date + split_part(t.name, '-', 1)::time AS output_hour
            FROM output_details od
            JOIN times t ON t.id = od.time_id
            JOIN production_lines fl ON fl.id = od.from_line_id
            JOIN departments d ON d.id = fl.department_id
            WHERE od.deleted_at IS NULL
                AND t.deleted_at IS NULL
                AND d.process_no = 2
        )
        SELECT hb.hour_ago, COALESCE(SUM(obt.good_qty), 0) AS output
        FROM hour_buckets hb
        LEFT JOIN output_by_time obt
            ON obt.output_hour >= hb.bucket_start
            AND obt.output_hour < hb.bucket_start + INTERVAL '1 hour'
        GROUP BY hb.hour_ago
        ORDER BY hb.hour_ago DESC
    """, nativeQuery = true)
    List<Object[]> outputLast48Hrs();
}
