import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getAllMilkingByAnimalId} from "../../../backend/milkingService";
import errors from "../../common/components/Errors";
import {MoonLoader} from "react-spinners";
import ReactEcharts from "echarts-for-react";

const AnimalMilkingChart = () => {

    const {animalId} = useParams();
    const [showChart, setShowChart] = useState(true);
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);

    let optionDefault = {
        title: {
            text: '',
            left: 'center',
            textStyle: {
                fontSize: 12
            }
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            valueFormatter: (value) => value + ' kg'
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
        series: [

        ]
    };
    const [option, setOption] = useState(optionDefault);

    useEffect(() => {
        const id = Number(animalId);
        if (isLoading) {
            // get chartData
            getAllMilkingByAnimalId(id, (milkingDtos) => {
                console.log(milkingDtos);
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
                };
                let sumLiters = 0;
                milkingDtos.forEach((dto) => {
                    sumLiters += dto.liters;
                    data.data.push(dto.liters);
                    optionDefault.xAxis.data.push(dto.date);
                });
                optionDefault.series.push(data);
                optionDefault.title.text = `Average daily production: ${(sumLiters/milkingDtos.length).toFixed(2)} liters`;
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
                        <h4><b>Milk production <i className="fas fa-caret-up"></i></b></h4>
                    }
                    {!showChart &&
                        <h4><b>Milk production <i className="fas fa-caret-down"></i></b></h4>
                    }
                </div>
            </div>
            {showChart &&
                <ReactEcharts option={option} lazyUpdate={true}/>
            }
        </div>
    );
}

export default AnimalMilkingChart;