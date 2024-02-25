import {useEffect, useState} from "react";
import {getMeatProductionEvolution} from "../../../backend/weighingService";
import {MoonLoader} from "react-spinners";
import ReactEcharts from "echarts-for-react";


const GeneralMeatProductionChart = () => {

    const [isLoading, setIsLoading] = useState(true);
    const [data, setData] = useState([]);
    const [backendErrors, setBackendErrors] = useState(null);
    const [showChart, setShowChart] = useState(true);
    let optionDefault = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            valueFormatter: (value) => value + ' kg'
        },
        title: {
            text: '',
            left: 'center',
            textStyle: {
                fontSize: 12
            }
        },
        toolbox: {
            feature: {
                dataView: { show: true, readOnly: true },
                magicType: { show: true, type: ['line', 'bar'] },
                restore: { show: true },
            }
        },
        legend: {},
        dataZoom: [
            {
                show: true,
            },
            {
                type: 'inside',
                show: true
            }
        ],
        xAxis: {
            type: 'category',
            data: []
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value} kg'
            }
        },
        series: []
    }
    const [option, setOption] = useState(optionDefault);

    useEffect(() => {
        if (isLoading) {
            getMeatProductionEvolution((chartData) => {
                console.log(chartData);
                setData(chartData);
                let data = {
                    data: [],
                    type: 'line',
                    smooth: true,
                    itemStyle: {
                        color: '#fc5858'
                    },
                    lineStyle: {
                        color: '#00B63E'
                    }
                }
                let sumKilos = 0;
                chartData.forEach((dto) => {
                    sumKilos += dto.kilos;
                    data.data.push(dto.kilos);
                    optionDefault.xAxis.data.push(dto.date);
                });
                optionDefault.series.push(data);
                setOption(optionDefault);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            })
        }
    }, []);

    if (isLoading) {
        return (
            <div className="row justify-content-center">
                <div className="col-1 text-center">
                    <MoonLoader color="#97C99D" />
                </div>
            </div>
        );
    }

    return (
        <div className="card card-chart mt-3">
            <div className="row">
                <div className="col-12 ml-4 mt-2 chart-title" onClick={e => setShowChart(!showChart)}>
                    {showChart &&
                        <h4><b>Weight evolution of animals form meat production <i className="fas fa-caret-up"></i></b></h4>
                    }
                    {!showChart &&
                        <h4><b>Weight evolution of animals form meat production <i className="fas fa-caret-down"></i></b></h4>
                    }
                </div>
            </div>
            {showChart && data.length > 0 &&
                <ReactEcharts option={option} lazyUpdate={true}/>
            }
            {showChart && data.length === 0 &&
                <div className="row justify-content-center">
                    <div className="col-11">
                        <div className="alert alert-primary" role="alert">
                            There are not weighings for meat production
                        </div>
                    </div>
                </div>
            }
        </div>
    );
}

export default GeneralMeatProductionChart;